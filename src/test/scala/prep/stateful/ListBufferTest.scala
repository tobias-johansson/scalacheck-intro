package prep.stateful

import org.scalacheck.Gen
import org.scalacheck.Prop._
import org.scalatest._
import org.scalatest.prop.Checkers

import scala.collection.mutable.ListBuffer

class ListBufferTest extends FreeSpec with Matchers with Checkers {

  implicit val config = PropertyCheckConfig(minSuccessful = 1000)

  "ListBuffer count" in check {

    sealed trait Action
    case object Add    extends Action
    case object Remove extends Action

    val actions = Gen.listOf(Gen.oneOf(Add, Remove))

    forAll(actions) { actions: List[Action] =>
      // Given
      //
      val list = ListBuffer[Int]()

      // When
      //
      actions.foreach {
        case Add    => list += 1
        case Remove => list -= 1
      }

      // Then
      //
      val count = actions.foldLeft(0) {
        case (c, Add)    => c + 1
        case (0, Remove) => 0
        case (c, Remove) => c - 1
      }
      count == list.size
    }
  }

}
