package stateful

import org.scalacheck.Gen
import org.scalacheck.Prop._
import org.scalatest._
import org.scalatest.prop.Checkers

import scala.util.Try

class StorageServiceTest extends FreeSpec with Matchers with Checkers {

  implicit val config = PropertyCheckConfig(minSuccessful = 1000)

//  "StorageService consistent elem count" in check {
//
//   }
}
