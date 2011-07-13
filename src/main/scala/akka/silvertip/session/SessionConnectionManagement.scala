/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package akka.silvertip.session

import akka.actor.{Actor, ActorRef}
import akka.silvertip._
import silvertip.{Connection, Message, MessageParser}

case object Start

trait SessionConnectionManagement[T] extends SessionConnectionManagementConfig { this: Actor =>
  protected var connection: ActorRef = _
  override def preStart { self ! Start }
  protected def sessionConnectionManagement: Receive = {
    case Start => {
      connection = Silvertip.createConnection(new ConnectionParameters(
        new MessageParserFactory[T] {
          def create = newMessageParser
        },
        Actor.actorOf(new Actor {
          var session: Option[Session] = None
          def receive: Receive = {
            case Connected(connection) => {
              session = Some(newSession)
              session.foreach(_.login(connection))
            }
            case Idle(connection) => {
              session.foreach(_.keepAlive(connection))
            }
            case Recv(connection: Connection[_], message: Any) => {
              session.foreach { session =>
                session.receive(connection, message)
              }
            }
            case Disconnected(connection: Connection[_]) => {
              if (session.isDefined) self.reply(session.get.delayUntilReconnect) else 0
            }
            case message: SilvertipMessage => Unit
          }
        }).start, hostname, port
      ))
    }
  }
  def newMessageParser: MessageParser[T]
  def newSession: Session
}

trait SessionConnectionManagementConfig {
  def hostname: String
  def port: Int
}
