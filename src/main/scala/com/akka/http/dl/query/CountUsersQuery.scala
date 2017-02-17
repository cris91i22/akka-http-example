package com.akka.http.dl.query

import com.akka.http.dl.TableRegistry
import com.akka.http.dl.modelinteract.ModelInteractUser
import com.akka.http.dl.storage.StorageQuery0
import com.akka.http.dl.utils.ExtendedPostgresDriver.api._

import scala.concurrent.{ExecutionContext, Future}

class CountUsersQuery(val db: Database, val tRegistry: TableRegistry)
                     (implicit val modelInteract: ModelInteractUser,
                      val ec: ExecutionContext) extends StorageQuery0[Int] {

  override def run(): Future[Int] = db.run(tRegistry.users.length.result)

}
