package com.akka.http.dl.modelinteract

import com.akka.http.dl._
import com.akka.http.model._

trait ModelInteractUser extends ModelInteract[User] {
  type D = UserDAO
  type Q = Users
}

object ModelInteractUser extends ModelInteractUser {
  implicit def upConvert(dao: UserDAO): User = User(
    dao.id,
    dao.name,
    dao.email
  )

  implicit def downConvert(model: User): UserDAO = UserDAO(
    model.id,
    model.name,
    model.email
  )

  def table(tRegistry: TableRegistry) = tRegistry.users
}

trait ModelInteractMessage extends ModelInteract[Message] {
  type D = MessageDAO
  type Q = Messages
}

object ModelInteractMessage extends ModelInteractMessage {
  implicit def upConvert(dao: MessageDAO): Message = Message(
    dao.id,
    dao.text,
    dao.userId,
    dao.recipientId
  )

  implicit def downConvert(model: Message): MessageDAO = MessageDAO(
    model.id,
    model.text,
    model.userId,
    model.recipientId
  )

  def table(tRegistry: TableRegistry) = tRegistry.messages
}
