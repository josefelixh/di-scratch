package di.scratch

import di.scratch.component._
import ApplicationContext._

object Application extends App with ContextSwithcher {
  override val env = Dev
  
  context.userService
  
  println (userService.findAll mkString(","))
}

object Test extends App with TestingContext {
  println (userService.findAll mkString(","))
}

trait Context extends
  UserRepositoryComponent with
  UserServiceComponent

trait LiveContext extends Context {
  override lazy val userRepository = new DefaultUserRepository
  override lazy val userService = new DefaultUserService
}

trait TestingContext extends Context {
  
  override lazy val userRepository = new UserRepository {
    override def findAll = List()
  }
  override lazy val userService = new UserService {
    override def findAll = List()
  }
}

trait ContextSwithcher {
  val env: Environment
  
  lazy val context: Context = env match {
    case Dev => new TestingContext {}
    case Prod => new LiveContext {}
  }
}

sealed trait Environment
case object Prod extends Environment
case object Dev extends Environment


  
  

