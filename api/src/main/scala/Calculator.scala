package cashpad.calculator

import cashpad.models.Totals

case class ParsedPad(
  people: List[String],
  lines: List[Line])

case class Line(
  desc: String,
  payers: Map[String, Double],
  spenders: List[String])

import scala.util.parsing.combinator._

object PadParsers extends RegexParsers {
  override val whiteSpace = """[ \t]+""".r

  def desc: Parser[String] = """[^(]+""".r
  def payer: Parser[(String, Double)] = person ~ """\d+(\.\d*)?""".r ^^ { case p ~ v =>
    (p, v.toDouble)
  }
  def payers: Parser[Map[String, Double]] = """\(""".r ~ payer.+ ~ """\)""".r ^^ { case _ ~ ps ~ _ =>
    ps.toMap
  }
  def line: Parser[Line] = desc ~ payers ~ person.+ ^^ { case d ~ ps ~ sps =>
    Line(d, ps, sps)
  }
  def person = """[A-Za-z]+""".r
  def people: Parser[List[String]] = """@people""".r ~ person.+ ^^ { case _ ~ pp => pp }
  def pad: Parser[ParsedPad] = people ~ """\n+""".r ~ (line <~ """\n+""".r).* ^^ { case ps ~ _ ~ ls =>
    ParsedPad(ps, ls)
  }

  def apply(input: String): Either[String, ParsedPad] = parseAll(pad, input) match {
    case Success(result, _) => Right(result)
    case failure : NoSuccess => Left(failure.msg)
  }
}

object compute {
  def apply(parsed: ParsedPad): Totals = Totals (
    parsed.lines.foldLeft(parsed.people.map(p => (p, 0 : Double)).toMap) { case (vals, line) =>
      val tot = line.payers.map(_._2).sum
      val each = tot / line.spenders.length
      vals.map { case (name, bal) =>
        val plus: Double = line.payers.get(name).getOrElse(0)
        val minus: Double = if (line.spenders.contains(name)) each else 0
        (name, bal + plus - minus)
      }
    })
}
