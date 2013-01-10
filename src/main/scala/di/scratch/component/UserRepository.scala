package di.scratch.component

import di.scratch.domain.User

trait UserRepositoryComponent {
  val userRepository: UserRepository

  trait UserRepository {
    def findAll: List[User]
  }

  class DefaultUserRepository extends UserRepository {
    implicit def String2User(string: String) = User(string)
    val users: List[User] = List("User1", "User2")

    override def findAll = users
  }
}