package com.akka.http.services

import akka.actor.{Actor, ActorRef, Props}
import com.akka.http.dl.storage.Storage
import com.akka.http.model.Message
import com.akka.http.services.MessageServiceActor._
import com.akka.http.utils.AppError
import com.akka.http.utils.fenele.EitherNel
import com.typesafe.scalalogging.LazyLogging

import scala.util.{Failure, Success}
import scalaz.syntax.nel._
import scalaz.{-\/, \/-}

class MessageServiceActor(storage: Storage) extends Actor with LazyLogging {

  override def receive: Receive = {
    case m: MessageServiceParams => receiveLocal(m, sender)
  }

  private def receiveLocal(m: MessageServiceParams, senderStash: ActorRef) = {
    import context.dispatcher

    m match {
      case CreateMessage(text, from, to) => storage.messages.create(Message(None, text, from, to)).onComplete {
        case Success(r) => senderStash ! CreateObjectReturn[Message](\/-(r))
        case Failure(ex) => senderStash ! CreateObjectReturn[Message](-\/(AppError("NOT CREATED", "Message was not created", Some(ex)).wrapNel))
      }
    }
  }

}

sealed trait MessageServiceParams
sealed trait MessageServiceResult

object MessageServiceActor {
  def props(storage: Storage) = Props(new MessageServiceActor(storage))

  case class CreateMessage(text: String, from: Int, to: Int) extends MessageServiceParams


  case class CreateObjectReturn[T](result: EitherNel[AppError, T]) extends MessageServiceResult
}
