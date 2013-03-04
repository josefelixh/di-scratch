package di.scratch.component

import di.scratch.domain.User

trait UserRepositoryComponent {
  def userRepository: UserRepository

  trait UserRepository {
    def findAll: List[User]
  }
}