package prep.stateful

import org.scalacheck.Gen
import org.scalacheck.Prop._
import org.scalatest._
import org.scalatest.prop.Checkers
import stateful.StorageService

import scala.util.Try

class StorageServiceTest extends FreeSpec with Matchers with Checkers {

  implicit val config = PropertyCheckConfig(minSuccessful = 1000)

  sealed trait Action
  case object Add    extends Action
  case object Remove extends Action
  case object Login  extends Action
  case object Logout extends Action

  val actions = Gen.listOf(Gen.oneOf(Add, Remove, Login, Logout))

  "StorageService consistent elem count" in check {

    forAll(actions) { actions: List[Action] =>
      // Given
      //
      val storage = new StorageService[String]

      // When
      //
      actions.foreach {
        case Login  => storage.login()
        case Logout => storage.logout()
        case Add    => Try(storage.add("foo"))
        case Remove => Try(storage.remove("foo"))
      }

      // Then
      //
      sealed trait Access
      case object Granted extends Access
      case object Denied  extends Access

      type State = (Access, Int)

      val (_, count) =
        actions.foldLeft[State]((Denied, 0)) {

          case ((Granted, c), Add)    => (Granted, c + 1)
          case ((Granted, 0), Remove) => (Granted, 0)
          case ((Granted, c), Remove) => (Granted, c - 1)

          case ((Granted, c), Logout) => (Denied, c)
          case ((Granted, c), _)      => (Granted, c)

          case ((Denied, c), Login) => (Granted, c)
          case ((Denied, c), _)     => (Denied, c)
        }

      count == storage.size

    }
  }
}
