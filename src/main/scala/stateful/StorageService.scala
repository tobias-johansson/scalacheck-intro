package stateful

import scala.collection.mutable.ListBuffer

class StorageService[T] {

  // State

  private var loggedIn = false
  private var content  = ListBuffer[T]()

  // Update

  def add(thing: T): Unit = restricted {
    content += thing
  }

  def remove(thing: T): Unit = {
    content -= thing
  }

  def size = content.size

  // Access

  def login(): Unit  = loggedIn = true
  def logout(): Unit = loggedIn = false

  private def restricted[A](block: => A): A = {
    if (loggedIn) block
    else throw new RuntimeException("Access denied")
  }

}
