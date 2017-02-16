package com.akka.http.dl

import utils.ExtendedPostgresDriver.api._

trait TableRegistry {
  def users: TableQuery[Users]
}
