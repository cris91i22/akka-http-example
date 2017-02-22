package com.akka.http.api.responses

import com.akka.http.model._

case class RetrieveUsersWithMessages(user: User,
                                     messages: List[Message])
