package jose.hackerdetector

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import jose.hackerdetector.context.UnitTestContext
import org.mockito.Mockito._
import org.mockito.Matchers._
import jose.hackerdetector.context.UnitTestContext

class HackerDetectorTest extends FlatSpec with ShouldMatchers {

  "The HackerDetector" should "not return suspicious ip (normal activity)" in new HackerDetectorImpl
    with UnitTestContext with TestData {

    stub(context.lineParser.parse("a failure")).toReturn(parsedFailure)
    stub(context.lineParser.parse("a success")).toReturn(parsedSuccess)
    stub(context.failuresRegister.failureThresold).toReturn(failureThresold)
    stub(context.failuresRegister.failuresInWindow).toReturn(normalTrafficFailures)

    (1 to 10) foreach { failure =>
      parseLine("a failure") should be === null
      parseLine("a success") should be === null
    }

    verify(context.failuresRegister, times(10)).purgeFailuresNotInWindow(parsedFailure.get.time)
    verify(context.failuresRegister, times(10)).register(parsedFailure.get)

  }

  it should "return suspicious ip (more than n failures within x minutes)" in new HackerDetectorImpl
    with UnitTestContext with TestData {

    stub(context.lineParser.parse("a failure")).toReturn(parsedFailure)
    stub(context.lineParser.parse("another failure")).toReturn(anotherParsedFailure)
    stub(context.failuresRegister.failureThresold).toReturn(failureThresold)
    stub(context.failuresRegister.failuresInWindow).toReturn(hackerTrafficFailures)

    parseLine("a failure") should be === parsedFailure.get.ip
    parseLine("another failure") should be === null

  }

  trait TestData {

    val failureThresold = 5

    val parsedFailure = Some(SIGNIN_FAILURE("192.168.0.1", 701, "Badger"))
    val anotherParsedFailure = Some(SIGNIN_FAILURE("192.168.0.2", 701, "Badger"))
    val parsedSuccess = None

    val normalTrafficFailures = Seq(
      SIGNIN_FAILURE("192.168.0.1", 0, "Badger"),
      SIGNIN_FAILURE("192.168.0.1", 100, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 150, "Badger"),
      SIGNIN_FAILURE("192.168.0.1", 200, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 250, "Badger"),
      SIGNIN_FAILURE("192.168.0.1", 300, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 350, "Badger"),
      SIGNIN_FAILURE("192.168.0.1", 400, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 450, "Badger"),
      SIGNIN_FAILURE("192.168.0.1", 500, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 550, "Badger"),
      SIGNIN_FAILURE("192.168.0.1", 600, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 650, "Badger"),
      SIGNIN_FAILURE("192.168.0.1", 700, "Badger"))

    val hackerTrafficFailures = Seq(
      SIGNIN_FAILURE("192.168.0.1", 401, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 450, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 550, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 552, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 554, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 556, "Badger"),
      SIGNIN_FAILURE("192.168.0.1", 557, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 558, "Badger"),
      SIGNIN_FAILURE("192.168.0.1", 559, "Badger"),
      SIGNIN_FAILURE("192.168.0.1", 600, "Badger"),
      SIGNIN_FAILURE("192.168.0.2", 650, "Badger"),
      SIGNIN_FAILURE("192.168.0.1", 700, "Badger"))
  }
}