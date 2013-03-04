package di.scratch.context

import di.scratch.component._
import jose.hackerdetector.lineparser.LineParserComponent

object DevContext extends DefaultContext {
  
  override lazy val userRepository = ???
  override lazy val userService = ???
  override lazy val lineParser = ???
}