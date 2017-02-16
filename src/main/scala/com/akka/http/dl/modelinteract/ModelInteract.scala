package com.akka.http.dl.modelinteract

import com.akka.http.dl._
import com.akka.http.dl.utils.ExtendedPostgresDriver.api._

trait ModelInteract[T] {
  type D <: StandardDAOBase[D] // DAO Type
  type Q <: WithStandardID[D] // Table Type

  implicit def upConvert(d: D): T
  implicit def downConvert(t: T): D
  def table(tRegistry: TableRegistry): TableQuery[Q]
  def tableWithReturn(tRegistry: TableRegistry) = {
    val t = table(tRegistry)
    t returning t.map(_.id) into ((obj, id) => obj.withID(id))
  }
}

object ModelInteract {
  implicit val modelInteractUser = ModelInteractUser
}
