package jose.hackerdetector.failuresregister.impl

import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers
import jose.hackerdetector.context.UnitTestContext
import jose.hackerdetector.SIGNIN_FAILURE


class FailuresRegisterComponentImplSpec extends FlatSpec with MustMatchers {
  
  sealed trait UnderTest extends FailuresRegisterComponentImpl {
    override val failuresRegister = new FailuresRegisterImpl{}
  }

  "FailuresRegister" should "purge redundant failures correctly" in new UnderTest {
    (1 to 5) foreach { i =>
      failuresRegister.register(SIGNIN_FAILURE("IP", 0, "USERNAME"))
    }
    failuresRegister.failuresInWindow.size must be === 5
    failuresRegister.purgeRedundantFailures("IP")
    failuresRegister.failuresInWindow.size must be === 4
  }
  
  it should "purge correctly failures out of window" in new UnderTest {
    (1 to 5) foreach { i =>
      failuresRegister.register(SIGNIN_FAILURE("IP", 0, "USERNAME"))
    }
    failuresRegister.failuresInWindow.size must be === 5
    
    (1 to 5) foreach { i =>
      failuresRegister.register(SIGNIN_FAILURE("IP", 301, "USERNAME"))
    }
    failuresRegister.failuresInWindow.size must be === 10
    
    failuresRegister.purgeFailuresNotInWindow(301)
    failuresRegister.failuresInWindow.size must be === 5
  }
}
    
    
