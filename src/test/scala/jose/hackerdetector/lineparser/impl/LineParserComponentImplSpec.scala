package jose.hackerdetector.lineparser.impl

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import jose.hackerdetector.HackerDetectorImpl
import jose.hackerdetector.context.UnitTestContext
import org.mockito.Mockito._
import jose.hackerdetector.SIGNIN_FAILURE

class LineParserComponentImplSpec extends FlatSpec with ShouldMatchers {
  
  sealed trait UnderTest extends LineParserComponentImpl {
    override val lineParser = new LineParserImpl
  } 

  "The LineParser" should "parse signin failures correctly" in new UnderTest {
    lineParser.parse("127.0.0.1,0,SIGNIN_FAILURE,John Moore") should be === Some(SIGNIN_FAILURE("127.0.0.1", 0, "John Moore"))
  }
  
  it should "not parse signin success" in new UnderTest {
    lineParser.parse("127.0.0.1,0,SIGNIN_SUCCESS,John Moore") should be === None
  }
  
  it should "throw IllegalArgumentException if entry is invalid" in new UnderTest {
    evaluating { lineParser.parse("Hello World!") } should produce [IllegalArgumentException]
    evaluating { lineParser.parse("127.0.0.1,invalid,SIGNIN_FAILURE,John Moore") } should produce [IllegalArgumentException]
    evaluating { lineParser.parse("127.0.0.1,invalid,SIGNIN_SUCCESS,John Moore") } should produce [IllegalArgumentException]
    evaluating { lineParser.parse("127.0.0.1,0,INVALID,John Moore") } should produce [IllegalArgumentException]
  }
}