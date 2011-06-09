package akka.silvertip

import silvertip.Connection

case object Connect
case object Reconnecting

case class Connected[T](connection: Connection[T])
case class Disconnected[T](connection: Connection[T])
case class GarbledMessage(message: String, data: Array[Byte])
case class Idle[T](connection: Connection[T])
case class Recv(connection: Connection[_], message: Any)
case class Send(connection: Connection[_], message: Any)
