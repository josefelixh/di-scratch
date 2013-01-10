package di.scratch.component

import di.scratch.domain.User

trait UserServiceComponent {
  this: UserRepositoryComponent => 
  val userService: UserService
  
  trait UserService {
    def findAll: List[User]
  }
  
  class DefaultUserService extends UserService {
      override def findAll = userRepository.findAll
  }
}