package com.akka.http.model

case class User(id: Option[Int] = None,
                name: String,
                email: String)

case class Message(id: Option[Int] = None,
                   text: String,
                   userId: Int,
                   recipientId: Int)