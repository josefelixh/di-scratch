package jose.hackerdetector.failuresregister.impl

import jose.hackerdetector.failuresregister.FailuresRegisterComponent
import jose.hackerdetector.SIGNIN_FAILURE

trait FailuresRegisterComponentImpl extends FailuresRegisterComponent {

  trait FailuresRegisterImpl extends FailuresRegister {

    val window = 5 * 60 //5 minutes in seconds 
    val failureThresold = 5

    val failures = collection.concurrent.TrieMap.empty[Int, List[SIGNIN_FAILURE]]
    def failuresWindow = failures.keys.toList.sorted
    def failuresInWindow = failures.values.flatten

    def purgeFailuresNotInWindow(time: Int) {
      failuresWindow filterNot { inWindow(time) } foreach { failures.remove }
    }

    def purgeRedundantFailures(ip: String) {
      val failureTimes = failures.filter { case (_, list) => list.exists(_.ip == ip) }.keys
      val olderFailure = failureTimes.toList.sorted.head
      val (matched, others) = failures(olderFailure).partition(_.ip == ip)

      failures.update(olderFailure, others ::: matched.tail)
    }

    def inWindow(time: Int): Int => Boolean = _ > (time - window)

    def register(signinFailure: SIGNIN_FAILURE) {
      failures.get(signinFailure.time) map { currentList =>
        failures.update(signinFailure.time, signinFailure :: currentList)
      } getOrElse {
        failures.put(signinFailure.time, signinFailure :: Nil)
      }
    }
  }
}