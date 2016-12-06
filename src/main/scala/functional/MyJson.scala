package functional

object MyJson {

  case class Report(value: Int, message: String)

  def toJson(r: Report): String =
    s"""{"value":${r.value},"message":"${r.message}"}"""

}
