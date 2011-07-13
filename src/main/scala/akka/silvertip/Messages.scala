package akka.silvertip

import silvertip.Connection

abstract class SilvertipMessage

case object Connect extends SilvertipMessage
case object Reconnecting extends SilvertipMessage

case class Connected[T](connection: Connection[T]) extends SilvertipMessage
case class Disconnected[T](connection: Connection[T]) extends SilvertipMessage
case class GarbledMessage(message: String, data: Array[Byte]) extends SilvertipMessage
case class Idle[T](connection: Connection[T]) extends SilvertipMessage
case class Recv(connection: Connection[_], message: Any) extends SilvertipMessage
case class Send(connection: Connection[_], message: Any) extends SilvertipMessage
