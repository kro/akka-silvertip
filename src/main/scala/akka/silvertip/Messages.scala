package akka.silvertip

import silvertip.Connection

case object Connect

case class Disconnected[T](connection: Connection[T])
case class Connected[T](connection: Connection[T])
case class GarbledMessage(message: String, data: Array[Byte])
case class Idle[T](connection: Connection[T])
