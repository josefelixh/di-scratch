package jose.hackerdetector

trait HackerDetector {
  def parseLine(line: String): String
}
trait HackerDetectorImpl extends HackerDetector {
  
  case class SIGNIN_FAILURE(ip: String, time: Int, user: String)
  
  val window = 5 * 60 //5 minutes in seconds 
  val failureThresold = 5
  
  val failures = collection.concurrent.TrieMap.empty[Int, List[SIGNIN_FAILURE]]
  def failuresWindow = failures.keys.toList.sorted
  def failuresInWindow = failures.values.flatten
  
  override def parseLine(line: String) = parse (line) map { failure =>
    purgeFailuresNotInWindow(failure.time)
    register(failure)
    if (suspicious(failure)) {
      purgeRedundantFailures(failure.ip)
      failure.ip 
    } else null
  } getOrElse(null)
  
  def purgeFailuresNotInWindow(time: Int) {
    failuresWindow filterNot { inWindow(time) } foreach { failures.remove }
  }
  
  def purgeRedundantFailures(ip: String) {
    val failureTimes = failures.filter { case (_, list) => list.exists(_.ip == ip) }.keys
    val olderFailure = failureTimes.toList.sorted.head 
    val (matched, others) = failures(olderFailure).partition(_.ip == ip)
    
    failures.update(olderFailure, others ::: matched.tail)
  }
  
  def inWindow(time: Int): Int => Boolean =  _ > (time - window)
  
  def register(signinFailure: SIGNIN_FAILURE) {
    failures.get(signinFailure.time) map { currentList =>
      failures.update(signinFailure.time, signinFailure :: currentList)
    } getOrElse {
      failures.put(signinFailure.time, signinFailure :: Nil)
    }
  }
  
  val suspicious: SIGNIN_FAILURE => Boolean = { failure =>
    failuresInWindow.filter(_.ip == failure.ip).size == failureThresold 
  }
  
  val parse: String => Option[SIGNIN_FAILURE] = _ split (",") match {
      case x if notValid(x) => throw new IllegalArgumentException
      case Array(ip, failureTime, "SIGNIN_FAILURE", user) => Some(SIGNIN_FAILURE(ip, failureTime.toInt, user))
      case _ => None
  }
  
  val notValid: Array[_] => Boolean = _.length != 4
}