package akka.silvertip

import akka.actor.ActorRef

class ConnectionParameters[T](
  val messageParserFactory: MessageParserFactory[T], 
  val listener: ActorRef,
  val reconnectDelayMsec: Int,
  val hostname: String, 
  val port: Int
)
