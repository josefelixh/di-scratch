package di.scratch.component.impl

import di.scratch.component.UserServiceComponent
import di.scratch.component.UserRepositoryComponent

trait UserServiceComponentImpl extends UserServiceComponent {
  this: UserRepositoryComponent =>
    override lazy val userService = new UserServiceImpl
    
    class UserServiceImpl extends UserService {
      override def findAll = userRepository.findAll
    }
}