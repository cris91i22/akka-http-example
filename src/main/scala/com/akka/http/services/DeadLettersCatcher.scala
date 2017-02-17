package com.akka.http.services

import akka.actor.{Actor, ActorLogging, DeadLetter, Props}
import com.typesafe.scalalogging.LazyLogging

class DeadLettersCatcher extends Actor with LazyLogging{

  override def receive: Receive = {
    case DeadLetter(msg, sdr, rcpt) =>
      logger.warn(s"You have dead letters message: $msg , sender: $sdr and recipient: $rcpt")
  }

}

object DeadLettersCatcher {
  def props = Props(classOf[DeadLettersCatcher])
}
