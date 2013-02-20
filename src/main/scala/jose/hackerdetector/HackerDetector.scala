package jose.hackerdetector

trait HackerDetector {
  def parseLine(line: String): String
}
trait HackerDetectorImpl extends HackerDetector {
  
  case class SIGNIN_FAILURE(ip: String, time: Int, user: String)
  
  val failures = collection.concurrent.TrieMap.empty[Int, List[SIGNIN_FAILURE]]
  
  override def parseLine(line: String) = check (parsed (line)) getOrElse (null)
  
  def now = System.currentTimeMillis() / 1000 //in seconds
  val window = 5 * 60 
  def windowAgo = now - window 
  val failureThresold = 5
  
  val inWindow: ((Int, _)) => Boolean = { case (time, _) => time > windowAgo }
  
  val check: Option[SIGNIN_FAILURE] => Option[String] = _ map { failure =>
    if (failures.values.flatten.filter(_.ip == failure.ip).size >= failureThresold)
      Some(failure.ip)
    else None
  } getOrElse(None)
  
  val register: SIGNIN_FAILURE => SIGNIN_FAILURE = { signinFailure =>
    failures.filterNot { inWindow }.keys.toList.foreach { failures.remove }
    failures.get(signinFailure.time) map { currentList =>
      failures.update(signinFailure.time, signinFailure :: currentList)
    } getOrElse (failures.put(signinFailure.time, signinFailure :: Nil))
    signinFailure
  }
  
  val notValid: Array[_] => Boolean = _.length != 4
  
  val parsed: String => Option[SIGNIN_FAILURE] = { _ split (",") match {
      case x if notValid(x) => throw new IllegalArgumentException
      case Array(ip, failureTime, "SIGNIN_FAILURE", user) if inWindow((failureTime.toInt, None)) => 
        Some(register(SIGNIN_FAILURE(ip, failureTime.toInt, user)))
      case _ => None
    }
  }
  
  

}

