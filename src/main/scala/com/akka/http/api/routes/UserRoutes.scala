package com.akka.http.api.routes

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.pattern.ask
import akka.util.Timeout
import com.akka.http.api.request.UserRequest
import com.akka.http.model.{Message, User}
import com.akka.http.services.MessageServiceActor.CreateObjectReturn
import com.akka.http.services.UserServiceActor.RetrieveUsersWithMessages
import com.akka.http.utils.fenele.EitherNel
import com.akka.http.utils.{AppError, AutoMarshaller}
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Try}
import scalaz.{-\/, \/-}

trait UserRoutes extends AutoMarshaller with Results with LazyLogging {
  import com.akka.http.api.converters.RequestConverters._
  import com.akka.http.api.converters.ResponseConverters._

  val userService: ActorRef
  implicit val userServiceTimeout: Timeout

  private val createUser = (pathEnd & post) {
    entity(as[UserRequest]) { record =>
      implicit val tryHandler: PartialFunction[Try[EitherNel[AppError, User]], StandardRoute] = {
        case Success(r) => r match {
          case -\/(a) => complete(StatusCodes.BadRequest, errorReturnObject(a))
          case \/-(b) => complete(StatusCodes.OK, b)
        }
      }

      onCompleteWithHandling((userService ? convertUser(record)).mapTo[CreateObjectReturn[User]].map(_.result))
    }
  }

  private val retrieveUsrWithMessages = (pathEnd & get) {
    implicit val tryHandler: PartialFunction[Try[EitherNel[AppError, Map[User, Seq[Message]]]], StandardRoute] = {
      case Success(r) => r match {
        case -\/(a) => complete(StatusCodes.BadRequest, errorReturnObject(a))
        case \/-(b) => val r = convertToResponse(b)
          complete(StatusCodes.OK, r)
      }
    }

    onCompleteWithHandling((userService ? RetrieveUsersWithMessages).mapTo[CreateObjectReturn[Map[User, Seq[Message]]]].map(_.result))
  }

  val userRoutes: Route = pathPrefix("user"){
    createUser ~
      retrieveUsrWithMessages
  }

}