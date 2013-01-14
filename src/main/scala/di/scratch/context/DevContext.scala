package di.scratch.context

import di.scratch.component.impl.UserRepositoryComponentImpl
import di.scratch.component.UserServiceComponent

object DevContext extends 
  UserRepositoryComponentImpl with
  UserServiceComponent