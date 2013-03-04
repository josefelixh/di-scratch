package jose.hackerdetector.lineparser

import jose.hackerdetector._

trait LineParserComponent {
  
  def lineParser: LineParser
  
  trait LineParser {
    def parse(line: String): Option[SIGNIN_FAILURE]
  }

}