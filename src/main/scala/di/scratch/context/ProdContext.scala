package di.scratch.context

import di.scratch.component.UserRepositoryComponent
import di.scratch.component.UserServiceComponent

object ProdContext extends 
  UserRepositoryComponent with
  UserServiceComponent