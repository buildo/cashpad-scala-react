package cashpad

import scala.slick.driver.MySQLDriver.simple._
import io.buildo.ingredients.logging.Level

import tables._

import akka.actor.ActorSystem
object CreateTables extends App
                with io.buildo.base.IngLoggingModule
                with SlickDatabaseModule {

  implicit def globalExecutionContext: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  lazy val projectName = "cashpad"

  private val log = logger("CreateTables")
  Slick.withSession { implicit session =>
    (pads.ddl).create
    log.info("Created tables")
  }
}


