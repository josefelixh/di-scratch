package jose.hackerdetector

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import jose.hackerdetector.context.UnitTestContext
import org.mockito.Mockito._
import org.mockito.Matchers._
import jose.hackerdetector.context.UnitTestContext

class HackerDetectorTest extends FlatSpec with ShouldMatchers {

  "The HackerDetector" should "not return suspicious ip (normal activity)" in new HackerDetectorImpl with UnitTestContext {
    
    stub(context.lineParser.parse(any[String])).toReturn(Some(SIGNIN_FAILURE(s"192.168.0.1", 0, "Badger Snake")))
    stub(context.failuresRegister.failureThresold).toReturn(5)
    stub(context.failuresRegister.failuresInWindow).toReturn(Seq()) 
    
    (1 to 10) foreach { failure =>
      parseLine("valid line") should be === null
    }
    
    verify(context.failuresRegister, times(10)).purgeFailuresNotInWindow(any[Int])
    verify(context.failuresRegister, times(10)).register(any[SIGNIN_FAILURE])
    
  }

  it should "return suspicious ip (more than n failures within x minutes)" in new HackerDetectorImpl with UnitTestContext {
    
    val inWindowLine = "inWindow line"
    val outOfWindowLine = "out of window line"  
    
    stub(context.lineParser.parse(inWindowLine)).toReturn(Some(SIGNIN_FAILURE(s"192.168.0.1", 150, "Badger Snake")))
    stub(context.lineParser.parse(outOfWindowLine)).toReturn(Some(SIGNIN_FAILURE(s"192.168.0.1", 500, "Badger Snake")))
    stub(context.failuresRegister.failureThresold).toReturn(5)
    
    (1 to 5 + 2).foreach { i =>
      if (i >= 5) {
        when(context.failuresRegister.failuresInWindow).thenReturn(Seq(SIGNIN_FAILURE(s"192.168.0.1", 150, "Badger Snake"), SIGNIN_FAILURE(s"192.168.0.1", 150, "Badger Snake"), SIGNIN_FAILURE(s"192.168.0.1", 150, "Badger Snake"), SIGNIN_FAILURE(s"192.168.0.1", 150, "Badger Snake"), SIGNIN_FAILURE(s"192.168.0.1", 150, "Badger Snake")))
        parseLine(inWindowLine) should be === "192.168.0.1"
      } else {
        when(context.failuresRegister.failuresInWindow).thenReturn(Seq())
        parseLine(inWindowLine) should be === null 
      }
    }
    
    when(context.failuresRegister.failuresInWindow).thenReturn(Seq())
    parseLine(outOfWindowLine) should be === null
  }
}