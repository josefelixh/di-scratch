package di.scratch

import di.scratch.component._
import di.scratch.context._
import di.scratch.domain.User

object Application extends App with Context {
  override val env = Dev
  println(context.userService.findAll mkString (","))
}

trait ProdContext extends UserRepositoryComponent
  with UserServiceComponent {

  override lazy val userRepository = new DefaultUserRepository
  override lazy val userService = new DefaultUserService

}

trait DevContext extends UserRepositoryComponent
  with UserServiceComponent {

  override lazy val userRepository = new UserRepository {
    implicit def String2User(string: String) = User(string)
    override def findAll = List("TestUser1", "TestUser2")
  }
  override lazy val userService = new DefaultUserService
}




  
  

