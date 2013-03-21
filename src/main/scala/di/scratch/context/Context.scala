package di.scratch.context

import di.scratch.component._
import di.scratch._
import di.scratch.Application._

sealed trait Environment
case object Prod extends Environment
case object Dev extends Environment
case object Default extends Environment

object ContextConfig {
  
  val components = Map[Environment, DefaultComponents](
      Prod -> ProdComponents,
      Dev -> DevComponents
    ).withDefault(_ => new DefaultComponents{})
  
  val environments = Map[String, Environment](
    "prod" -> Prod,
    "dev" -> Dev
  ).withDefault(_ => Default)
}

trait Context {
  import ContextConfig._
  
  val environmentProperty = System.getProperty("env", "not-set").toLowerCase()
  
  val environment = environments(environmentProperty)
  
  lazy val component = components(environment)
  
}

