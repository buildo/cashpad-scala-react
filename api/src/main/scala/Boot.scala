package cashpad

import akka.actor.ActorSystem
import akka.io.IO

import spray.can.Http

object Boot extends io.buildo.base.Boot
  with io.buildo.base.IngLoggingModule
  with RouterModule {

  lazy val projectName = "cashpad"

  private val log = logger("Boot")
  val b = boot()

  lazy val actorSystem = b.system
}
