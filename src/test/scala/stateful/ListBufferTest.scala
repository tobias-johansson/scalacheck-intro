package stateful

import org.scalacheck.Gen
import org.scalacheck.Prop._
import org.scalatest._
import org.scalatest.prop.Checkers

import scala.collection.mutable.ListBuffer

class ListBufferTest extends FreeSpec with Matchers with Checkers {

  implicit val config = PropertyCheckConfig(minSuccessful = 1000)

//  "ListBuffer size" in check {
//
//  }

}
