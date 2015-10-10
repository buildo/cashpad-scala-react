package cashpad

package models

case class Pad(_id: String, name: String, contents: String)
case class PadName(_id: String, name: String)

case class CreatePad(name: String)
case class UpdatePad(contents: String)

case class Totals(totals: Map[String, Double])
