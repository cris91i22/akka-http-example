package com.akka.http.api.routes

import akka.actor.ActorRef
import akka.http.scaladsl.server.StandardRoute
import akka.pattern.ask
import akka.util.Timeout
import com.akka.http.api.request.MessageRequest
import com.akka.http.utils.AutoMarshaller
import akka.http.scaladsl.model.StatusCodes._

import scala.util.{Success, Try}

trait MessageRoutes extends AutoMarshaller with Results {
  import com.akka.http.api.converters.MessageConverters._

  val messageService: ActorRef
  implicit val messageServiceTimeout: Timeout

  val create = (pathEnd & post) {
    entity(as[MessageRequest]) { record =>
      implicit val tryHandler: PartialFunction[Try[String], StandardRoute] = {
        case Success(_) => complete(Created, Map("nana" -> ""))
      }
      onCompleteWithHandling((messageService ? convert(record)).mapTo[String])
    }
  }

  val messageRoutes = pathPrefix("message"){
    create //~
  }

}
