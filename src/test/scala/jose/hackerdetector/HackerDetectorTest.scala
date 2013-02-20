package jose.hackerdetector

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class HackerDetectorTest extends FlatSpec with ShouldMatchers {

  "The HackerDetector" should "not return suspicious ip (normal activity)" in new HackerDetectorImpl {
    override val now = 0.toLong
    val testData = for (host <- 1 to 254) yield s"192.168.0.$host,0,SIGNIN_FAILURE,John Moore"
    testData.par map {
      parseLine(_) should be(null)
    }
  }

  it should "return suspicious ip (more than 5 failures within 5 minutes)" in new HackerDetectorImpl {
    override val now = 301.toLong
    val host = 1
    parseLine(s"192.168.0.$host,150,SIGNIN_FAILURE,John Moore") should be(null)
    parseLine(s"192.168.0.$host,150,SIGNIN_FAILURE,John Moore") should be(null)
    parseLine(s"192.168.0.$host,150,SIGNIN_FAILURE,John Moore") should be(null)
    parseLine(s"192.168.0.$host,150,SIGNIN_FAILURE,John Moore") should be(null)
    parseLine(s"192.168.0.$host,150,SIGNIN_FAILURE,John Moore") should be("192.168.0.1")
    parseLine(s"192.168.0.$host,150,SIGNIN_FAILURE,John Moore") should be("192.168.0.1")
    parseLine(s"192.168.0.$host,0,SIGNIN_FAILURE,John Moore") should be(null)
  }

  it should "throw IllegalArgumentException if entry is invalid" in new HackerDetectorImpl {
    evaluating { parseLine("Hello World!") } should produce [IllegalArgumentException]
    evaluating { parseLine("127.0.0.1,invalid,SIGNIN_FAILURE,John Moore") } should produce [IllegalArgumentException]
  }
}