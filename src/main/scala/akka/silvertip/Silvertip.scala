package akka.silvertip

import akka.actor.Actor
import akka.config.Supervision.OneForOneStrategy

object Silvertip {
  private val supervisor = Actor.actorOf(new SupervisorActor).start
  def createConnection[T](connectionParameters: ConnectionParameters[T]) = {
    val connection = Actor.actorOf(new ConnectionActor(connectionParameters))
    supervisor.startLink(connection)
    connection ! Connect
    connection
  }
  private class SupervisorActor extends Actor {
    self.faultHandler = OneForOneStrategy(List(classOf[Throwable]))
    def receive: Receive = { case _ => Unit }
  }
}
