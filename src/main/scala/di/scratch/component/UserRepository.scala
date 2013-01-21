package di.scratch.component

import di.scratch.domain.User

trait UserRepositoryComponent {
  lazy val userRepository: UserRepository = ???

  trait UserRepository {
    def findAll: List[User]
  }
}