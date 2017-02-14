package com.akka.http.api.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.RejectionHandler

trait Routes extends MessageRoutes {

  val rh = RejectionHandler.newBuilder().handle{
    case rejection => complete(InternalServerError, rejection.toString)
  }.result()

  lazy val routes =
    handleRejections(rh) {
      messageRoutes
    }

}
