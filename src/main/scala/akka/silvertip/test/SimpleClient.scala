package akka.silvertip.test

import akka.actor.Actor
import akka.silvertip._
import net.lag.logging.Logger

object SimpleClient extends App with SimpleServerClientConfig {
  Silvertip.createConnection(new ConnectionParameters(
    new MessageParserFactory[String] {
      def create = new SimpleMessageParser
    },
    Actor.actorOf(new Actor {
      def receive: Receive = { case message => logger.info("Message: %s", message) }
    }).start, "localhost", serverPort
  ))
  lazy val logger = Logger.get
}
