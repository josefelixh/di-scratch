package di.scratch.component

import di.scratch.domain.User

trait UserServiceComponent {
    
  def userService: UserService
  
  trait UserService {
    def findAll: List[User]
  }
}