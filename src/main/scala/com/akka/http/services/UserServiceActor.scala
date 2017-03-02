package com.akka.http.services

import akka.actor.{Actor, ActorRef, Props}
import com.akka.http.dl.storage.Storage
import com.akka.http.model.{Message, User}
import com.akka.http.services.UserServiceActor.{CreateUser, ObjectReturn, RetrieveUsersWithMessages}
import com.akka.http.utils.AppError
import com.akka.http.utils.fenele.EitherNel
import com.typesafe.scalalogging.LazyLogging

import scalaz.syntax.nel._
import scala.util.{Failure, Success}
import scalaz.{-\/, \/-}

class UserServiceActor(storage: Storage) extends Actor with LazyLogging {
  override def receive: Receive = {
    case m: UserServiceParams => receiveLocal(m, sender)
  }

  private def receiveLocal(m: UserServiceParams, senderStash: ActorRef) = {
    import context.dispatcher

    logger.debug(s"User service actor, path: ${self.path.name}")

    m match {
      case CreateUser(n, e) => storage.users.create(User(None, n, e)) onComplete {
        case Success(r) => senderStash ! ObjectReturn[User](\/-(r))
        case Failure(ex) => senderStash ! ObjectReturn[User](-\/(AppError("NOT CREATED", "Message was not created", Some(ex)).wrapNel))
      }
      case RetrieveUsersWithMessages => storage.retrieveUsersWithMessages.run().onComplete{
        case Success(r) => senderStash ! ObjectReturn[Map[User, Seq[Message]]](\/-(r))
        case Failure(ex) => senderStash ! ObjectReturn[Map[User, Seq[Message]]](-\/(AppError("NOT FOUND", "Users with messages were not found", Some(ex)).wrapNel))
      }
    }
  }

}

sealed trait UserServiceParams
sealed trait UserServiceResult

object UserServiceActor{
  def props(storage: Storage) = Props(new UserServiceActor(storage))

  case class CreateUser(name: String, email: String) extends UserServiceParams
  case object RetrieveUsersWithMessages extends UserServiceParams

  case class ObjectReturn[T](result: EitherNel[AppError, T]) extends UserServiceResult
}