package cashpad

import models._

import spray.json._

trait JsonSerializerModule extends io.buildo.base.MonadicCtrlJsonModule
  with io.buildo.base.JsonModule {

  object CashpadJsonProtocol extends AutoProductFormat
    with DefaultJsonProtocol {

    import io.buildo.ingredients.jsend.JSendJsonProtocol._

    import io.buildo.ingredients.jsend.dsl._

    implicit val PadSI = `for`[Pad] serializeOneAs("pad") andMultipleAs("pads")
    implicit val PadNameSI = `for`[PadName] serializeOneAs("padName") andMultipleAs("padNames")
    implicit val CreatePadSI = `for`[CreatePad]
    implicit val UpdatePadSI = `for`[UpdatePad]
    implicit val TotalsSI = `for`[Totals] serializeOneAs("totals")
  }
}
