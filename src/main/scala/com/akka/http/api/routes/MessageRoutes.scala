package com.akka.http.api.routes

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import akka.pattern.ask
import akka.util.Timeout
import com.akka.http.api.request.MessageRequest
import com.akka.http.model.Message
import com.akka.http.services.MessageServiceActor.CreateObjectReturn
import com.akka.http.utils.fenele.EitherNel
import com.akka.http.utils.{AppError, AutoMarshaller}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Try}
import scalaz.{-\/, \/-}

trait MessageRoutes extends AutoMarshaller with Results with LazyLogging {
  import com.akka.http.api.converters.MessageConverters._

  val messageService: ActorRef
  implicit val messageServiceTimeout: Timeout

  val create = (pathEnd & post) {
    entity(as[MessageRequest]) { record =>
      implicit val tryHandler: PartialFunction[Try[EitherNel[AppError, Message]], StandardRoute] = {
        case Success(r) => r match {
          case -\/(a) => complete(StatusCodes.BadRequest, errorReturnObject(a))
          case \/-(b) => complete(StatusCodes.OK, b)
        }
      }

      onCompleteWithHandling((messageService ? convert(record)).mapTo[CreateObjectReturn[Message]].map(_.result))
    }
  }

  val messageRoutes = pathPrefix("message"){
    create //~
  }

}
