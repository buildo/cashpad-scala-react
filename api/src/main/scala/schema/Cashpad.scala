package cashpad

import scala.slick.driver.MySQLDriver.simple._

import cashpad.models.Pad

package tables {

  class Pads(tag: Tag) extends Table[Pad](tag, "pads") {
    def _id = column[String]("id", O.PrimaryKey)
    def name = column[String]("name")
    def contents = column[String]("contents")

    def * = (_id, name, contents) <> (Pad.tupled, Pad.unapply)
  }

}

package object tables {
  val pads = TableQuery[Pads]
}
