package di.scratch

import di.scratch.component._
import di.scratch.context._
import di.scratch.domain.User
import di.scratch.component.impl.UserRepositoryComponentImpl

object Application extends App with Context {
  override val env = Prod
  println(context.userService.findAll mkString (","))
}

trait ProdContext extends UserRepositoryComponent
  with UserServiceComponent {

  override lazy val userRepository = new DefaultUserRepository
  override lazy val userService = new DefaultUserService

}

trait DevContext extends 
	UserRepositoryComponentImpl with
	UserServiceComponent {

  override lazy val userRepository = new UserRepositoryImpl
  override lazy val userService = new DefaultUserService
}




  
  

