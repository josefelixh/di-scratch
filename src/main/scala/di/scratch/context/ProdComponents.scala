package di.scratch.context

import di.scratch.component.impl._
import jose.hackerdetector.lineparser.LineParserComponent

object ProdComponents extends DefaultComponents with 
  UserRepositoryComponentImpl with
  UserServiceComponentImpl {
  
  override lazy val userRepository = new UserRepositoryImpl{}
  override lazy val userService = new UserServiceImpl{}
  
}