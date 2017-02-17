package com.akka.http.api.request

case class MessageRequest(text: String,
                          userId: Int,
                          recipientId: Int){
  require(text nonEmpty)
}
