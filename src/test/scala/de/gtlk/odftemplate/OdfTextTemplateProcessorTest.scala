package de.gtlk.odftemplate

import org.scalatest.funsuite.AnyFunSuite

/**
 * Test for the open office text template processor.
 */
class OdfTextTemplateProcessorTest extends AnyFunSuite {
  
  import TestDocumentParameters._
  import OdfTextTemplateProcessorTest._
  
  test("Replacement of Parameters") {
    val processor = processorForTestDocument()
    
    // Convenience function for shorter creation of replacements
    def rep(parameter: TemplateParameter, value: String) = TextReplacement(parameter, value)
    
    
    // The paragraph containing POSITION and PRICE should be repeated three times,
    // In each repetition the fields should be replaced with the given values
    val orderItems = List(List("First Item", "80,00"),
                          List("Second Item", "45,00"),
                          List("Third Item", "5,00"));
    val paragraphAlteration = new ParagraphRepetition(List(POSITION, PRICE),
             orderItems);
    
    val textReplacements = List(
            rep(NAME, "Max Mustermann"),
            rep(STREET, "Test Street 10"),
            rep(CITY, "12345 Test City"),
            rep(SUM, "130,00"),
            rep(DATE, "5.4.2010"),
            rep(INVOICE_NUMBER, "123456"))
    
    val alterations = paragraphAlteration :: textReplacements
    
    processor.performAlterations(alterations)
    
    val fullText = processor.extractFullText()

    // The text should not contain any parameters any longer but
    // should contain all replacements
    textReplacements.foreach { replacement =>
      if (replacement.parameter.isContainedIn(fullText)) {
        fail("The parameter '" + replacement.parameter + "' has not been replaced in the template")
      }
      if (!fullText.contains(replacement.replacement)) {
        fail("The replacement '" + replacement.replacement + "' is not present in the result text")
      }
    }
    
    // Check that all order items are present in result document
    orderItems.flatten.foreach { item =>
      if (!fullText.contains(item)) {
        fail("The order item '" + item + "' is not present in the result template.")
      }
    }
    
  }
  
  test("No unknown parameters in test template") {
    checkNoUnknownParametersInTemplate(processorForTestDocument(), 
        TestDocumentParameters)
  }
  
  test("All Parameters present in test template") {
    checkParametersPresentInTemplate(processorForTestDocument(),
        TestDocumentParameters)
  }
}

object OdfTextTemplateProcessorTest {
  
  import org.scalatest.Assertions._
  
  /**
   * A test that can be used for testing the sanity of concrete document templates:
   * checks that only parameters defined in the given enum are present in the template.
   */
  def checkNoUnknownParametersInTemplate(processor: OdfTextTemplateProcessor,
      templateParameters: TemplateParameters) {
    val matcher = """\[\[([^\]]+)\]\]""".r
    
    var unknownParams = Set[String]()
    val knownParams = templateParameters.allParameters.map(_.name)
    
    processor.forAllTextNodesInDocument { textNode =>
      val foundParams = matcher.findAllIn( textNode.getTextContent).matchData.map(_.subgroups(0))
      foundParams.foreach( param =>
        if (!knownParams.contains(param)) {
          unknownParams = unknownParams + param
        }
      )
    }
    
    if (unknownParams.nonEmpty) {
      fail("The following unknown parameters are present in the template: " + 
          unknownParams.mkString(", "))
    }
  }
  
  /**
   * A test that can be used for testing the sanity of concrete document templates:
   * checks that all defined parameters are really present in the template.
   */
  def checkParametersPresentInTemplate(processor: OdfTextTemplateProcessor,
      templateParameters: TemplateParameters) {
    var allParameters = templateParameters.allParameters()

    processor.forAllTextNodesInDocument { textNode =>
      val text = textNode.getTextContent
      templateParameters.allParameters().foreach { parameter =>
        if (parameter.isContainedIn(text)) {
          allParameters = allParameters.filter(_ != parameter)
        }
      }
    }
    
    if (allParameters.nonEmpty) {
      fail("The following parameters are not present in the template: " + 
          allParameters.map(_.name).mkString(", "))
    }
  }
  
  /**
   * Creates a template processor for the test document in the classpath.
   */
  def processorForTestDocument(): OdfTextTemplateProcessor = OdfTextTemplateProcessor.fromStream(getClass.
        getResourceAsStream("/TestDocumentWithTemplateParameters.odt"))
}

/**
 * Parameters contained in the test document.
 */
object TestDocumentParameters extends TemplateParameters {
  final val NAME = param("Name")
  final val STREET = param("Street")
  final val CITY = param("City")
  final val DATE = param("Date")
  final val INVOICE_NUMBER = param("InvoiceNumber")
  final val POSITION = param("Position")
  final val PRICE = param("Price")
  final val SUM = param("Sum")
}
