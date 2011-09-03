package akka.silvertip

import akka.actor.Actor
import akka.config.Supervision.{Permanent, OneForOneStrategy}
import java.net.InetSocketAddress
import net.lag.logging.Logger
import silvertip.{Events, Connection}

private[silvertip] class ConnectionActor[T](params: ConnectionParameters[T]) extends Actor {
  private var connection: Option[Connection[T]] = None
  self.faultHandler = OneForOneStrategy(List(classOf[Throwable]))
  self.lifeCycle = Permanent
  import params._
  def receive: Receive = {
    case Connect => attemptToConnect
    case message => connection.foreach { connection => 
      listener ! Send(connection, message)
    }
  }
  override def postRestart(reason: Throwable) {
    attemptToConnect
  }
  override def preRestart(reason: Throwable) {
    disconnect
  }
  override def postStop {
    disconnect
  }
  private def attemptToConnect {
    logger.debug("SilvertipConnectionFactory#create -->")
    connection.foreach(_ => listener ! Reconnecting)
    connection = Some(Connection.attemptToConnect(new InetSocketAddress(hostname, port), messageParserFactory.create, 
      new Connection.Callback[T]() {
        def messages(connection: Connection[T], messages: java.util.Iterator[T]) {
          while (messages.hasNext) listener ! Recv(connection, messages.next)
        }
        def idle(connection: Connection[T]) { 
          listener ! Idle(connection)
        }
        def closed(connection: Connection[T]) {
          Actor.spawn {
            (listener !! Disconnected(connection)).asInstanceOf[Option[Int]].foreach(Thread.sleep(_))
            self ! Connect
          }
        }
        def garbledMessage(message: String, data: Array[Byte]) { 
          listener ! GarbledMessage(message, data)
        }
    }))
    logger.debug("  silvertip.Events#open")
    val events = Events.open(timeoutIntervalMsec)
    logger.debug("  silvertip.Events#register")
    events.register(connection.get)
    logger.debug("  EventsDispatchThread#start")
    new EventsDispatchThread(events).start
    listener ! Connected(connection.get)
  }
  private def disconnect {
    connection.foreach { connection => 
      if (!connection.isClosed) 
        connection.close 
    }
  }
  private def timeoutIntervalMsec = 100
  private def logger = Logger.get
}
