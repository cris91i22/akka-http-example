package com.akka.http.utils

import akka.util.Timeout

import scala.concurrent.duration._

trait ServicesTimeouts {

  implicit lazy val messageServiceTimeout = Timeout(5 seconds)
  implicit lazy val userServiceTimeout = Timeout(5 seconds)

}
