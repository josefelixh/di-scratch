package di.scratch.context

import di.scratch.component._
import jose.hackerdetector.lineparser.LineParserComponent

trait DefaultContext  extends 
  UserRepositoryComponent with
  UserServiceComponent with 
  LineParserComponent {
  
  override lazy val userRepository: UserRepository = ???
  override lazy val userService: UserService = ???
  override lazy val lineParser: LineParser = ???

}