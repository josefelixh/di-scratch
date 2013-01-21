package di.scratch.component

import di.scratch.domain.User

trait UserServiceComponent {
  this: UserRepositoryComponent => 
    
  lazy val userService: UserService = ???
  
  trait UserService {
    def findAll: List[User]
  }
}