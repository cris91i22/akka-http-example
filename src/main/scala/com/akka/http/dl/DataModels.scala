package com.akka.http.dl

trait StandardDAOBase[T] {
  def id: Option[Int]
  def withID(id: Int): T
}

case class MessageDAO(id: Option[Int] = None,
                      text: String,
                      userId: Int,
                      recipientId: Int) extends StandardDAOBase[MessageDAO] {
  def withID(id: Int) = this.copy(id = Some(id))
}

case class UserDAO(id: Option[Int] = None,
                   name: String,
                   email: String) extends StandardDAOBase[UserDAO] {
  def withID(id: Int) = this.copy(id = Some(id))
}