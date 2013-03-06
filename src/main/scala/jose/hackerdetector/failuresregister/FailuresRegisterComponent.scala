package jose.hackerdetector.failuresregister

import jose.hackerdetector.SIGNIN_FAILURE
import jose.hackerdetector.SIGNIN_FAILURE

trait FailuresRegisterComponent {
  
  def failuresRegister: FailuresRegister
  
  trait FailuresRegister {
    def register(failure: SIGNIN_FAILURE): Unit
    def purgeFailuresNotInWindow(time: Int): Unit
    def purgeRedundantFailures(ip: String): Unit
    def failuresInWindow: Iterable[SIGNIN_FAILURE]
    def failureThresold: Int
  }

}