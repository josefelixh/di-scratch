package jose.hackerdetector.context

import jose.hackerdetector.lineparser.LineParserComponent
import org.scalatest.mock.MockitoSugar
import di.scratch.context.DefaultContext
import di.scratch.context.Context

trait UnitTestContext {
  this: Context =>
    
  override lazy val context = UTContext
  
  object UTContext extends DefaultContext with MockitoSugar {
    
      override lazy val lineParser = mock[LineParser]
      
  }

  
}