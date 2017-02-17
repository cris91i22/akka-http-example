package com.akka.http.dl.storage

import com.akka.http.dl.TableRegistry
import com.akka.http.dl.modelinteract.ModelInteract
import com.akka.http.dl.query.CountUsersQuery
import com.akka.http.dl.utils.ExtendedPostgresDriver.api._
import com.akka.http.model.{Message, User}

import scala.concurrent.ExecutionContext

class ExampleStorage(val db: Database, val tRegistry: TableRegistry)
                    (implicit val ec: ExecutionContext) extends Storage {

  import ModelInteract._

  val users: BasicObjectStorage[User] = new ObjectStorage[User](db, tRegistry)
  val messages: BasicObjectStorage[Message] = new ObjectStorage[Message](db, tRegistry)

  val countUsers: CountUsersQuery = new CountUsersQuery(db, tRegistry)

}