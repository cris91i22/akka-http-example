package com.akka.http.dl

import com.akka.http.dl.utils.ExtendedPostgresDriver.api._
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

class ExampleDB(config: Config = ConfigFactory.load)(implicit ec: ExecutionContext) extends LazyLogging {

  final val configName = "db"
  final val instance = {
    val i = createInstance(config)
    migrate(i)
    i
  }

  def shutdown() {
    try {
      logger.debug("Shutting down db instance.")
      Await.result(instance.shutdown, 2 seconds)
    }
    catch {
      case e: Throwable => logger.error(s"DB instance shutdown failed", e)
    }
  }

  private def migrate(i: Database) = {
//    try {
//      logger.info("Running migration...")
//      MigrationAssistant.migrate(i)
//    }
//    catch {
//      case e: Throwable => {
//        logger.error("Migration failed!")
//        throw(e)
//      }
//    }
  }

  private def createInstance(config: Config) = {
    try {
      logger.debug(s"Using Configuration: $config")
      Database.forConfig(configName, config)
    }
    catch {
      case e: Throwable => {
        logger.error("DB Instance could not be created.", e)
        throw e
      }
    }
  }
}