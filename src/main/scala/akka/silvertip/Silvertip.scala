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
  def shutdownAll {
    import scala.collection.JavaConversions._
    supervisor.linkedActors.values.foreach  { actor =>
      actor.stop
      supervisor.unlink(actor)
    }
    supervisor.stop
  }
  private class SupervisorActor extends Actor {
    self.faultHandler = OneForOneStrategy(List(classOf[Throwable]))
    def receive: Receive = { case _ => Unit }
  }
}
