package com.akka.http.utils

import com.typesafe.scalalogging.LazyLogging

import scalaz._
import Scalaz._

/**
  * Type mappings for use across the library.
  */
package object fenele {
  import scala.concurrent.{Future, ExecutionContext}

  type EitherNel[E, T] = NonEmptyList[E] \/ T
  type FutureEitherNel[E, T] = Future[EitherNel[E, T]]
  type Fenele[T] = FutureEitherNel[AppError, T]

  /**
    * Implicit class to provide convenience methods.
    */
  implicit class FeneleT[T](f: Fenele[T]) extends LazyLogging {
    def eitherT(): EitherT[Future, NonEmptyList[AppError], T] = EitherT(f)
    def logged(message: String)(implicit ec: ExecutionContext): Fenele[T] = {
      f.foreach(t => logger.debug(s"$message - $t"))
      f
    }
  }

  object Fenele {
    /**
      * Convenience method for creating an error return when there's no Future.
      *
      * @param error The AppError to wrap up into a Fenele object
      * @return The error wrapped in a nice package for use with MonadTransformers where needed.
      */
    def apply[T](error: AppError): Fenele[T] = Future.successful(EitherNel(error))

    /**
      * Convenience method for creating a valid Fenele when there's no future.
      *
      * @param value The value to wrap up into a Fenele object
      * @return The value wrapped in a nice package for use with MonadTransformers where needed.
      */
    def apply[T](value: T): Fenele[T] = Future.successful(EitherNel(value))

    /**
      * Convenience method for mapping an optional to a valid Fenele. This simplifies for comprehensions
      * where we grab a thing then need to use an optional value within that thing but only if the
      * optional actually has a value. In essence it lifts an actual value out of an optional.
      *
      * @param value The optional value we are evaluating.
      * @tparam T The type of the Option.
      * @return Fenele of the type T.
      */
    def apply[T](value: Option[T]): Fenele[T] = value match {
      case Some(t) => Fenele(t)
      case None => Fenele(AppError("missingValue", "Expected valid value but found none."))
    }

    /**
      * Convenience method for creating a valid Fenele from a standard Future.
      *
      * @param  unlifted The value to convert from Future[T] to Future[NonEmptyList[AppError] \/ T] based on the result of the
      *                      Future (success or failure)
      * @return lifted value
      */
    def apply[T](unlifted: Future[T])(implicit ec: ExecutionContext): Fenele[T] = unlifted.map(EitherNel(_)).recover { case t: Throwable =>
      EitherNel(AppError("unknownFailure", "Unknown Failure", Some(t)))
    }
  }

  object EitherNel {
    /**
      * Convenience method to create an EitherNel from a single error value.
      *
      * @param error The AppError to wrap up into an EitherNel object
      * @return The error wrapped in a nice package.
      */
    def apply[T](error: AppError): EitherNel[AppError, T] = -\/(error.wrapNel)

    /**
      * Convenience method to create an EitherNel from a valid value.
      *
      * @param value The value to wrap up into an EitherNel object
      * @return The value wrapped in a nice package.
      */
    def apply[T](value: T): EitherNel[AppError, T] = \/-(value)

  }
}
