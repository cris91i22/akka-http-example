package com.akka.http.services

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.akka.http.services.MessageServiceActor._

class MessageServiceActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case m: MessageServiceParams => receiveLocal(m, sender)

  }

  private def receiveLocal(m: MessageServiceParams, senderStash: ActorRef) = {
//    import context.dispatcher

    m match {
      case CreateMessage(t, u) => log.info("YEAHHHHHHHHHHHHH") ; senderStash ! "OK"
    }
  }

}

sealed trait MessageServiceParams
sealed trait MessageServiceResult

object MessageServiceActor {
  def props = Props(new MessageServiceActor())

  case class CreateMessage(text: String, user: String) extends MessageServiceParams
}
