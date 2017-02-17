package com.akka.http.dl

import utils.ExtendedPostgresDriver.api._

trait WithStandardID[T] extends Table[T] {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
}

class Users(tag: Tag) extends Table[UserDAO](tag, "users") with WithStandardID[UserDAO] {
  def name = column[String]("name")
  def email = column[String]("email")

  def * = (id.?, name, email) <> (UserDAO.tupled, UserDAO.unapply)
}

class Messages(tag: Tag) extends Table[MessageDAO](tag, "messages") with WithStandardID[MessageDAO] {
  def text = column[String]("text")
  def userId = column[Int]("userId")
  def recipientId = column[Int]("recipientId")

  def userFk = foreignKey("FLOW_USR_FK", userId, TableQueries.users)(_.id, onDelete=ForeignKeyAction.Cascade)
  def recipientFk = foreignKey("FLOW_RCP_FK", recipientId, TableQueries.users)(_.id, onDelete=ForeignKeyAction.Cascade)

  def * = (id.?, text, userId, recipientId) <> (MessageDAO.tupled, MessageDAO.unapply)
}

object TableQueries extends TableRegistry {

  val users = TableQuery[Users]
  val messages = TableQuery[Messages]

  // This list should always be ordered such that a DDL created from the sequence is ordered correctly (i.e. If table A has a
  // foreign key to table B then table B should appear first in the sequence).
  val all = Seq(
    users,
    messages
  )

  def createActions() = all.map(_.schema.create)

  def dropActions() = all.reverse.map(_.schema.drop)
}
