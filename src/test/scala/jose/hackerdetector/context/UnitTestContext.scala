package jose.hackerdetector.context

import jose.hackerdetector.lineparser.LineParserComponent
import org.scalatest.mock.MockitoSugar
import di.scratch.context.DefaultContext
import di.scratch.context.Context
import org.mockito.Mockito

trait UnitTestContext {
  this: Context =>
    
  override lazy val context = new DefaultContext with MockitoSugar {
    
      override lazy val lineParser = mock[LineParser]
      override lazy val failuresRegister = mock[FailuresRegister]
      
  }
}