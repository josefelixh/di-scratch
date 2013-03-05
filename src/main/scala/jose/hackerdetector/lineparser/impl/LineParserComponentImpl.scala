package jose.hackerdetector.lineparser.impl

import jose.hackerdetector.lineparser.LineParserComponent
import jose.hackerdetector.SIGNIN_FAILURE

trait LineParserComponentImpl extends LineParserComponent {

  class LineParserImpl extends LineParser {
    
    override def parse(line: String) = line split (",") match {
      case Array(ip, failureTime, action, user) if !failureTime.matches("[+-]?\\d+") => throw new IllegalArgumentException
      case Array(ip, failureTime, "SIGNIN_FAILURE", user) => Some(SIGNIN_FAILURE(ip, failureTime.toInt, user))
      case Array(_,_,"SIGNIN_SUCCESS",_) => None
      case _ => throw new IllegalArgumentException
    }
    
  }
}