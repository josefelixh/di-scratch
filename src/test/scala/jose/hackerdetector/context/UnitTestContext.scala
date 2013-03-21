package jose.hackerdetector.context

import jose.hackerdetector.lineparser.LineParserComponent
import org.scalatest.mock.MockitoSugar
import di.scratch.context.DefaultComponents
import di.scratch.context.Context
import org.mockito.Mockito

trait UnitTestContext extends Context {
    
  override lazy val component = new DefaultComponents with MockitoSugar {
    
      override lazy val lineParser = mock[LineParser]
      override lazy val failuresRegister = mock[FailuresRegister]
      
  }
}