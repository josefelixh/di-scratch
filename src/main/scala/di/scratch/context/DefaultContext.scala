package di.scratch.context

import di.scratch.component._
import jose.hackerdetector.lineparser.LineParserComponent
import jose.hackerdetector.failuresregister.FailuresRegisterComponent

trait DefaultContext  extends 
  UserRepositoryComponent with
  UserServiceComponent with 
  LineParserComponent with
  FailuresRegisterComponent {
  
  override lazy val userRepository: UserRepository = ???
  override lazy val userService: UserService = ???
  override lazy val lineParser: LineParser = ???
  override lazy val failuresRegister: FailuresRegister = ???

}