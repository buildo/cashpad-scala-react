package cashpad

import scala.slick.driver.MySQLDriver.simple._

import scala.concurrent.Future

import models._

import scalaz._
import Scalaz._
import scalaz.EitherT._

import scala.concurrent.ExecutionContext.Implicits.global

trait PadControllerModule extends io.buildo.base.MonadicCtrlModule
  with SlickDatabaseModule {

  object padController {
    private def uuid() = java.util.UUID.randomUUID.toString

    def getAllPadNames: FutureCtrlFlow[List[PadName]] = eitherT {
      Slick.withAsyncSession { implicit session =>
        tables.pads.list
      }.map { ps =>
        ps.map { p =>
          PadName(p._id, p.name)
        }.point[CtrlFlow]
      }
    }

    def getById(id: String): FutureCtrlFlow[Pad] = eitherT {
      Slick.withAsyncSession { implicit session =>
        tables.pads.filter(_._id === id).firstOption
      }.map { fo =>
        fo.map(\/-(_)).getOrElse(-\/(CtrlError.NotFound))
      }
    }

    def create(createPad: CreatePad): FutureCtrlFlow[Pad] = eitherT {
      val newPad = Pad(_id = uuid(), name = createPad.name, contents = "")
      Slick.withAsyncSession { implicit session =>
        tables.pads += newPad
      }.map { _ =>
        newPad.point[CtrlFlow]
      }
    }

    def update(id: String, updatePad: UpdatePad): FutureCtrlFlow[Unit] = eitherT {
      Slick.withAsyncSession { implicit session =>
        tables.pads.filter(_._id === id).map(_.contents).update(updatePad.contents)
      }.map { _ =>
        ().point[CtrlFlow]
      }
    }

    def totals(id: String): FutureCtrlFlow[Totals] = {
      for {
        pad <- getById(id)
        z <- eitherT {
          {
            calculator.PadParsers(pad.contents) match {
              case Right(parsed) => calculator.compute(parsed).point[CtrlFlow]
              case Left(msg) => -\/(CtrlError.InvalidOperation(msg))
            }
          }.point[Future]
        }
      } yield (z)
    }
  }
}
