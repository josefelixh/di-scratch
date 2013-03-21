package jose.hackerdetector

import di.scratch.context.Context
import jose.hackerdetector.lineparser.LineParserComponent
import jose.hackerdetector.failuresregister.FailuresRegisterComponent
import di.scratch.context._

case class SIGNIN_FAILURE(ip: String, time: Int, user: String)

trait HackerDetector {
  def parseLine(line: String): String
}
trait HackerDetectorImpl extends HackerDetector with Context {

  private val lineParser = component.lineParser
  private val failuresRegister = component.failuresRegister
  
  val failureThresold = 5
  
  override def parseLine(line: String) = lineParser.parse(line) map { failure =>
    failuresRegister.purgeFailuresNotInWindow(failure.time)
    failuresRegister.register(failure)
    if (suspicious(failure)) {
      failuresRegister.purgeRedundantFailures(failure.ip)
      failure.ip 
    } else null
  } getOrElse(null)
  
  private val suspicious: SIGNIN_FAILURE => Boolean = { failure =>
    failuresRegister.failuresInWindow.filter(_.ip == failure.ip).size == failureThresold 
  }
}
