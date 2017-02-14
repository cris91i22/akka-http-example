package com.akka.http.api.request

case class MessageRequest(text: String,
                          from: String){
  require(text nonEmpty)
  require(from nonEmpty)
}
