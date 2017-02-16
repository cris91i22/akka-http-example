package com.akka.http.dl.utils

import com.github.tminglei.slickpg.{ExPostgresProfile, PgHStoreSupport}
import slick.basic.Capability
import slick.jdbc.JdbcCapabilities

trait ExtendedPostgresDriver extends ExPostgresProfile with PgHStoreSupport {
  // Add back `capabilities.insertOrUpdate` to enable native `upsert` support; for postgres 9.5+
  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + JdbcCapabilities.insertOrUpdate

  override val api = ExtendedAPI

  object ExtendedAPI extends API with HStoreImplicits
}

object ExtendedPostgresDriver extends ExtendedPostgresDriver
