package jose.hackerdetector

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import jose.hackerdetector.context.UnitTestContext
import org.mockito.Mockito._
import org.mockito.Matchers._
import jose.hackerdetector.context.UnitTestContext

class HackerDetectorTest extends FlatSpec with ShouldMatchers {

  "The HackerDetector" should "not return suspicious ip (normal activity)" in new HackerDetectorImpl with UnitTestContext {
    val testData = for (host <- 1 to 254) yield {
      val line = s"192.168.0.$host,$host,SIGNIN_FAILURE,John Moore"
      when(context.lineParser.parse(line)).thenReturn(Some(SIGNIN_FAILURE(s"192.168.0.$host", host, "Badger Snake")))
      line
    }
    
    testData.par foreach {
      parseLine(_) should be === null
    }
  }

  it should "return suspicious ip (more than 5 failures within 5 minutes)" in new HackerDetectorImpl with UnitTestContext {
    val line = "inWindow line"
    stub(context.lineParser.parse(line)).toReturn(Some(SIGNIN_FAILURE("192.168.0.1", 150, "Username")))
    
    val line2 = "out of window line"
    when(context.lineParser.parse(line2)).thenReturn(Some(SIGNIN_FAILURE("192.168.0.1", 500, "Username")))
    
    (1 to failureThresold + 2).foreach { i =>
      if (i >= failureThresold) parseLine(line) should be === "192.168.0.1"
      else parseLine(line) should be === null
    }
    
    parseLine(line2) should be === null
  }

  it should "purge redundant failures" in new HackerDetectorImpl with UnitTestContext {
    val line = "a line"
    stub(context.lineParser.parse(line)).toReturn(Some(SIGNIN_FAILURE("192.168.0.1", 150, "Username")))
    (1 to failureThresold - 1).foreach { _ => parseLine(line) should be === null }
    
    parseLine(line) should be === "192.168.0.1"
    failures(150).size should be === 4
    
    parseLine(line) should be === "192.168.0.1"
    failures(150).size should be === 4
  }
}