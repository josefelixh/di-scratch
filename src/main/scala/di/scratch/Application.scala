package di.scratch

import di.scratch.component._
import di.scratch.context._
import di.scratch.domain.User
import di.scratch.component.impl.UserRepositoryComponentImpl

object Application extends App with Context {
  println(component.userService.findAll mkString (","))
}



  
  

