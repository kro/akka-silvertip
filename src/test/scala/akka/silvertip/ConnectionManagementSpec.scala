package akka.silvertip

import akka.actor.{Actor, ActorRef}
import akka.silvertip.test.Specification
import scala.util.Random

/*
object ConnectionManagementSpec extends Specification {
  private var clientActor: ActorRef = _
  "Disconnected ConnectionManagement" should {
    val server = new SimpleServer
    doBefore { 
      startSimpleServer(server)
    }
    doAfter {
      server.connection.close
    }
    "create a connection when started" in {
      clientActor = Actor.actorOf(new SimpleClient).start
      waitUntil(isConnectionOpen(server))
      isConnectionOpen(server) mustBe true
    }
  }
  "Connected ConnectionManagement" should {
    val server = new SimpleServer
    doBefore { 
      startSimpleServer(server)
      clientActor = Actor.actorOf(new SimpleClient).start
      waitUntil(isConnectionOpen(server))
    }
    "create a connection when disconnected" in {
      server.connection.close
      waitUntil(isConnectionOpen(server) == false)
      isConnectionOpen(server) mustBe false
      startSimpleServer(server)
      waitUntil(isConnectionOpen(server))
    }
  }
  private def isConnectionOpen(server: SimpleServer) = {
    server.connection.isClosed == false
  }
  private def startSimpleServer(server: SimpleServer) = {
    new Thread(server).start 
  }
}
*/
