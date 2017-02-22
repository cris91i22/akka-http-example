package com.akka.http.api.converters

import com.akka.http.api.responses.RetrieveUsersWithMessages
import com.akka.http.model._

object ResponseConverters {

  def convertToResponse(response: Map[User, Seq[Message]]): List[RetrieveUsersWithMessages] = {
    response.map{ case (u, msgs) =>
      RetrieveUsersWithMessages(u, msgs.toList)
    }.toList
  }

}
