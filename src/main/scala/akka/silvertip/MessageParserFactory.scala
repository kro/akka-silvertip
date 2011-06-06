package akka.silvertip

import silvertip.MessageParser

trait MessageParserFactory[T] {
  def create: MessageParser[T]
}
