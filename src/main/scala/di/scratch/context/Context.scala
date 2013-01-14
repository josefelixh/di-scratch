package di.scratch.context

import di.scratch.component._
import di.scratch._
import di.scratch.Application._

sealed trait Environment
case object Prod extends Environment
case object Dev extends Environment

trait Context {
  lazy val context = resolveContext
  def resolveContext(implicit env: Environment) = {
    env match {
      case Prod => ProdContext
      case Dev => DevContext
    }
  }
}

