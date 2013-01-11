package di.scratch.context

import di.scratch.component._
import di.scratch._

sealed trait Environment
case object Prod extends Environment
case object Dev extends Environment

trait Context {
  val env: Environment = Dev
  
  lazy val context = env match {
    case Dev => new DevContext {}
    case Prod => new ProdContext {}
  }
}

