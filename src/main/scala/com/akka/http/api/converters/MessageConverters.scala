package com.akka.http.api.converters

import com.akka.http.api.request.MessageRequest
import com.akka.http.services.MessageServiceActor.CreateMessage

object MessageConverters {

  implicit def convert(request: MessageRequest): CreateMessage = {
    CreateMessage(text = request.text,
                  from = request.userId,
                  to = request.recipientId)
  }

}
