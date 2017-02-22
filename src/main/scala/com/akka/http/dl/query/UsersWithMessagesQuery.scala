package com.akka.http.dl.query

import com.akka.http.dl.{MessageDAO, TableRegistry, UserDAO}
import com.akka.http.dl.modelinteract.{ModelInteractMessage, ModelInteractUser}
import com.akka.http.dl.storage.StorageQuery0
import com.akka.http.dl.utils.ExtendedPostgresDriver.api._
import com.akka.http.model.{Message, User}

import scala.concurrent.{ExecutionContext, Future}

class UsersWithMessagesQuery(val db: Database, val tRegistry: TableRegistry)
                            (implicit val userModelInteract: ModelInteractUser,
                             messageModelInteract: ModelInteractMessage,
                             val ec: ExecutionContext) extends StorageQuery0[Map[User, Seq[Message]]] {

  private def transform(result: Seq[(UserDAO, MessageDAO)]) = {
    result.groupBy(_._1).map{case(u, list) =>
      userModelInteract.upConvert(u) -> list.map{case(_, m) => messageModelInteract.upConvert(m)}
    }
  }

  private def getQuery = {
    for {
      user <- tRegistry.users
      message <- tRegistry.messages if user.id == message.userId
    } yield (user, message)
  }

  override def run(): Future[Map[User, Seq[Message]]] = db.run(getQuery.result).map(transform)
}
