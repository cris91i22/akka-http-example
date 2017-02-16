package com.akka.http.dl.modelinteract

import com.akka.http.dl.{TableRegistry, UserDAO, Users}
import com.akka.http.model.User

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
