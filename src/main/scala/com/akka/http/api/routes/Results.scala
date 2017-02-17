package com.akka.http.api.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server._
import com.akka.http.utils.AppError
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.util.{Failure, Try}
import scalaz.NonEmptyList

private [routes] trait Results extends Directives with LazyLogging {

  private val commonFailures: PartialFunction[Try[_], StandardRoute] = {
    case Failure(ex) => complete(StatusCodes.InternalServerError, ex)
  }

  protected def onCompleteWithHandling[T](f: => Future[T])(implicit tryHandler: PartialFunction[Try[T], StandardRoute]): Route = {
    onComplete(f)(tryHandler orElse commonFailures)
  }

  /**
    * Utility method for generating the full error return object when encountering errors.
    *
    * @param errors The nel array of errors.
    * @return JsObject containing the encoded errors.
    */
  protected def errorReturnObject(errors: NonEmptyList[AppError]): String = {
    errors.foreach {
      case AppError(shortCode, message, Some(throwable)) => logger.error(s"$shortCode: $message", throwable)
      case AppError(shortCode, message, None) => logger.error(s"$shortCode: $message")
    }
    errors.toString()
  }

}
