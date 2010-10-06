package de.gtlk.odftemplate

import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.odftoolkit.odfdom.doc.text.OdfTextParagraph

/**
 * Interface for arbitrary open office text document manipulations.
 */
trait DocumentAlteration {
  
  /**
   * Returns the template parameter which must be contained
   * in a node so that this alteration must be performed.
   */
  def parameter: TemplateParameter
  
  /**
   * Performs the alteration for the given node and the paragraph in which
   * the node is contained.
   * This method is only called if the template parameter 
   * is present in the text node.
   */
  def alterNode(textNode: Node, paragraph: OdfTextParagraph)
}


/**
 * Replacement of a single variable with a String value.
 */
case class TextReplacement(parameter: TemplateParameter, replacement: String) extends DocumentAlteration {
  
  override def alterNode(textNode: Node, paragraph: OdfTextParagraph) = {
    parameter.replaceInNodeWith(replacement, textNode)
  }
}

/**
 * Replaces the parameter with the given String, or removes the entire paragraph
 * in which the parameter is contained if the replacement is None.
 */
case class TextReplacementOrParagraphRemoval(parameter: TemplateParameter, replacement: Option[String])
    extends DocumentAlteration {
  
  override def alterNode(textNode: Node, paragraph: OdfTextParagraph) = {
    replacement match {
      case Some(rep) => parameter.replaceInNodeWith(rep, textNode)
      case None => {
        val parent = paragraph.getParentNode
        parent.removeChild(paragraph)
      }
    }
  }
}

/**
 * Repeats the paragraphs containing the given parameters as many times as the list
 * of replacementIterations has elements.
 * Both given lists should not be empty!
 */
case class ParagraphRepetition(parameters: List[TemplateParameter], replacementIterations: List[List[String]]) extends DocumentAlteration {
  
  override def parameter = parameters.head
  
  override def alterNode(textNode: Node, paragraph: OdfTextParagraph) {
    val nextSibling = paragraph.getNextSibling
    val parent = paragraph.getParentNode
    
    // Create clones of the paragraph for each replacement iteration but the first
    replacementIterations.tail.foreach { replacements =>
      val paragraphCopy = paragraph.cloneNode(true)
      // Also works if nextSibling is null!
      parent.insertBefore(paragraphCopy, nextSibling)
    
      replaceInAllTextNodes(paragraphCopy, replacements)
    }
    
    // After creating the clones, replace parameters in the paragraph itself
    replaceInAllTextNodes(paragraph, replacementIterations.head)
  
    // Replaces all parameters with the given replacements in all
    // text nodes below the given paragraph node
    def replaceInAllTextNodes(line: Node, replacements: List[String]) {
      val paramsAndReplacements = parameters.zip(replacements)
      
      OdfTextTemplateProcessor.forAllTextNodes(line) { node =>
        paramsAndReplacements.foreach { paramAndReplacement =>
          val (param, replacement) = paramAndReplacement
          param.replaceInNodeWith(replacement, node)
        }
      }
    }
  }

}