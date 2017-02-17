package com.akka.http.utils

case class AppError(shortCode: String,
                    message: String,
                    root: Option[Throwable] = None) extends RuntimeException {
  val cause = root.orNull
  override def toString: String = s"AppError($shortCode, $message)"
}

object AppError {
  def apply(shortCode: String, messages: String): AppError = {
    apply(shortCode, messages, None)
  }
}
