package com.akka.http.dl.storage

import com.akka.http.dl._
import com.akka.http.dl.modelinteract.ModelInteract
import com.akka.http.dl.utils.ExtendedPostgresDriver.api._

import scala.concurrent.{ExecutionContext, Future}

class ObjectStorage[T](val db: Database, val tRegistry: TableRegistry)(
  implicit val modelInteract: ModelInteract[T], val ec: ExecutionContext) extends BasicObjectStorage[T] {

  def retrieve(id: Int): Future[Option[T]] = {
    val action = modelInteract.table(tRegistry).filter(_.id === id).result.headOption
    db.run(action).map(_.map(dm => modelInteract.upConvert(dm)))
  }
  def update(obj: T): Future[Int] = {
    val dao = modelInteract.downConvert(obj)
    dao.id.map { id =>
      val action = modelInteract.table(tRegistry).filter(_.id === id).update(dao)
      db.run(action)
    } getOrElse {
      val msg = s"Update requires a valid primary key. $obj"
      Future.failed(new Exception(msg))
    }
  }
  def create(obj: T): Future[T] = {
    val dao = modelInteract.downConvert(obj)
    val tableWithReturn = modelInteract.tableWithReturn(tRegistry)
    db.run(tableWithReturn += dao).map(dm => modelInteract.upConvert(dm))
  }
  def delete(id: Int): Future[Int] = {
    val action = modelInteract.table(tRegistry).filter(_.id === id).delete
    db.run(action)
  }
}
