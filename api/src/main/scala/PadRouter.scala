package cashpad

import models._

import spray.routing._
import spray.routing.Directives._
import spray.httpx.SprayJsonSupport._

import scala.concurrent.ExecutionContext.Implicits.global

trait PadRouterModule extends io.buildo.base.MonadicCtrlRouterModule
  with io.buildo.base.MonadicRouterHelperModule
  with io.buildo.base.ConfigModule
  with JsonSerializerModule
  
  with PadControllerModule {

  import CashpadJsonProtocol._
  import RouterHelpers._

  val padRoute = {
    pathPrefix("pads") {
      (get & pathEnd) (returns[List[PadName]].ctrl(padController.getAllPadNames _)) ~
      (get & path(Segment)) (returns[Pad].ctrl(padController.getById _)) ~
      (post & entity(as[CreatePad])) (returns[Pad].ctrl(padController.create _)) ~
      (post & path(Segment) & entity(as[UpdatePad])) (returns[Unit].ctrl(padController.update _)) ~
      (get & path(Segment / "totals")) (returns[Totals].ctrl(padController.totals _))
    }
  }
}
