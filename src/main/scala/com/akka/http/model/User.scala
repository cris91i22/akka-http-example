package com.akka.http.model

case class User(id: Option[Int] = None,
                name: String,
                email: String)
