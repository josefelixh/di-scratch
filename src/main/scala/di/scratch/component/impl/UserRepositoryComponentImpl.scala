package di.scratch.component.impl

import di.scratch.component.UserRepositoryComponent
import di.scratch.domain.User

trait UserRepositoryComponentImpl extends UserRepositoryComponent {
  
  override lazy val userRepository = new UserRepositoryImpl
  
  class UserRepositoryImpl extends UserRepository {
    implicit def String2User(string: String) = User(string)
    val users: List[User] = List("TestUser1", "TestUser2")
    override def findAll = users
  }

}