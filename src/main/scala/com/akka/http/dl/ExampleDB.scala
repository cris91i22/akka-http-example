package com.akka.http.dl

import akka.event.LoggingAdapter
import com.typesafe.config.{Config, ConfigFactory}
import utils.ExtendedPostgresDriver.api._
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

class ExampleDB(config: Config = ConfigFactory.load)(implicit ec: ExecutionContext){

  final val configName = "db"
  final val instance = {
    val i = createInstance(config)
    migrate(i)
    i
  }

  def shutdown() {
    try {
      println("Shutting down db instance.")
      Await.result(instance.shutdown, 2 seconds)
    }
    catch {
      case e: Throwable => println(s"DB instance shutdown failed: $e")
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
      println(s"Using Configuration: $config")
      Database.forConfig(configName, config)
    }
    catch {
      case e: Throwable => {
        println("DB Instance could not be created.")
        throw(e)
      }
    }
  }
}