package prep.functional

import functional.MyJson
import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalatest._
import org.scalatest.prop.Checkers

import scala.util.parsing.json._

class MyJsonTest extends FreeSpec with Matchers with Checkers {

  implicit val config = PropertyCheckConfig(minSuccessful = 10000)

  "MyJson produces valid json" in check {

    forAll { (value: Int, message: String) =>
      val json = MyJson.toJson(MyJson.Report(value, message))
      JSON.parseFull(json) == Some(Map("value" -> value, "message" -> message))
    }

  }

}
