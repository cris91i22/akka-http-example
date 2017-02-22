package com.akka.http.api.converters

import com.akka.http.api.request.{MessageRequest, UserRequest}
import com.akka.http.services.MessageServiceActor.CreateMessage
import com.akka.http.services.UserServiceActor.CreateUser

object RequestConverters {

  def convert(request: MessageRequest): CreateMessage = {
    CreateMessage(text = request.text,
                  from = request.userId,
                  to = request.recipientId)
  }

  def convertUser(request: UserRequest): CreateUser = {
    CreateUser(name = request.name,
                email = request.email)
  }

}
