package de.gtlk.odftemplate

import org.odftoolkit.odfdom.doc.OdfTextDocument
import org.odftoolkit.odfdom.doc.OdfDocument
import org.odftoolkit.odfdom.incubator.doc.text.OdfTextParagraph
import org.odftoolkit.odfdom.dom.element.text.TextPElement
import org.odftoolkit.odfdom.dom.OdfDocumentNamespace
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement
import org.w3c.dom.Node
import org.w3c.dom.NodeList

import java.io.InputStream
import java.io.OutputStream
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

/**
 * Processor for text templates based on Open Office Text documents.
 */
class OdfTextTemplateProcessor(val document: OdfTextDocument) {
  import OdfTextTemplateProcessor._
  
  // Content root of the document
  val root: OfficeTextElement = document.getContentRoot
  
  /**
   * Returns the full text of the document (without formatting).
   */
  def extractFullText(): String = {
    val result = new StringBuilder()

    forAllParagraphs(root) { paragraph =>
      forAllTextNodes(paragraph) { textNode =>
        result.append(textNode.getTextContent)
      }
      result.append("\n")
    }
    
    result.toString
  }
  
  /**
   * Performs all given document alterations on the document.
   */
  def performAlterations(alterations: List[DocumentAlteration]) {
    forAllParagraphs(root) { paragraph =>
      forAllTextNodes(paragraph) { textNode =>
      
        val text = textNode.getTextContent
        alterations.foreach { alteration =>
          if (alteration.parameter.isContainedIn(text)) {
            alteration.alterNode(textNode, paragraph)
          }
        }
      }
    }
  }
  
  /**
   * Iterates through all text nodes in the whole document.
   */
  def forAllTextNodesInDocument(function: Node => Unit) {
    forAllTextNodes(root)(function)
  }
  
  /**
   * Saves the document to a file.
   */
  def saveToFile(filename: String) {
    document.save(filename)
  }
  
  /**
   * Writes the document to an output stream.
   */
  def writeToOutputStream(out: OutputStream) {
    document.save(out)
  }
  
}


object OdfTextTemplateProcessor {
  
  // XPath instance with ODF Text namespace
  val XPATH: XPath = XPathFactory.newInstance().newXPath()
  XPATH.setNamespaceContext(OdfTextNamespaceContext)
  
  // Name of the Paragraph XML element
  val PARAGRAPH_NAME: String = TextPElement.ELEMENT_NAME.getQName
  
  
  /**
   * Creates a text template processor from a stream with a text template.
   * 
   */
  def fromStream(stream: InputStream): OdfTextTemplateProcessor = {
    new OdfTextTemplateProcessor(OdfDocument.loadDocument(stream).asInstanceOf[OdfTextDocument])
  }
  
  
  /**
   * Iterates all paragraphs in the given document.
   */
  def forAllParagraphs(root: OfficeTextElement)(function : OdfTextParagraph => Unit) {
    val paragraphs: NodeList = XPATH.evaluate("//" + PARAGRAPH_NAME, root,
          XPathConstants.NODESET).asInstanceOf[NodeList]
    
    (0 until paragraphs.getLength).foreach { paragraphNumber =>
      val paragraph = paragraphs.item(paragraphNumber).asInstanceOf[OdfTextParagraph]
      function(paragraph)
    }
  }
  
  /**
   * Iterates all text nodes below the given node (descendants).
   */
  def forAllTextNodes(node: Node)(function: Node => Unit) {
    val textNodes: NodeList = XPATH.evaluate("descendant::text()", node, 
          XPathConstants.NODESET).asInstanceOf[NodeList]
      
    (0 until textNodes.getLength).foreach { textNodeNumber =>
      val textNode = textNodes.item(textNodeNumber)
      function(textNode)
    }
  }
}