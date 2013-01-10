package di.scratch

import di.scratch.component._

object ApplicationContext extends
  UserRepositoryComponent with
  UserServiceComponent {
  
  override lazy val userRepository = new DefaultUserRepository
  override lazy val userService = new DefaultUserService
}