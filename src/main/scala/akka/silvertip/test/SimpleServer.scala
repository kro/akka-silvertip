package akka.silvertip.test

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import scala.collection.JavaConversions._
import silvertip.{Connection, MessageParser, Events}
import net.lag.logging.Logger

object SimpleServer extends App with SimpleServerClientConfig {
  logger.info("Waiting for connection...")
  val connection = Connection.accept(new InetSocketAddress(serverPort), new SimpleMessageParser, 
    new Connection.Callback[String]() {
      def messages(connection: Connection[String], messages: java.util.Iterator[String]) = {
        messages.foreach { message => logger.info("Message: %s", message) }
      }
      def idle(connection: Connection[String]) = Unit
      def closed(connection: Connection[String]) = Unit
      def garbledMessage(message: String, data: Array[Byte]) = Unit
    }
  )
  logger.info("Connection accepted")
  val events = Events.open(timeoutIntervalMsec)
  events.register(connection)
  events.dispatch
  logger.info("Connection closed")
  lazy val timeoutIntervalMsec = 100
  lazy val logger = Logger.get
}

class SimpleMessageParser extends MessageParser[String] {
  def parse(byteBuffer: ByteBuffer): String = {
    val buffer = new StringBuffer
    while (byteBuffer.hasRemaining)
      buffer.append(byteBuffer.get.asInstanceOf[Char])
    buffer.toString
  }
}
