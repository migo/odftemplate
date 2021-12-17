package de.gtlk.odftemplate

import org.w3c.dom.Node

/**
 * A template parameter that can be replaced in an Open Office Text document.
 * 
 * @author Michael Gottschalk
 */
case class TemplateParameter(name: String) {
    
  /**
   * Returns if the parameter is present in the given text.
   */
  def isContainedIn(text: String): Boolean = {
    text.contains(fullParameter)
  }
  
  /**
   * Replaces the parameter in the node with the given text
   * if it is present in the node.
   */
  def replaceInNodeWith(replacement: String, node: Node) {
    val text = node.getTextContent
    if (isContainedIn(text)) {
      val replaced = text.replace(fullParameter, replacement)      
      node.setTextContent(replaced)
    }
  }
  
  lazy val fullParameter: String = "[[" + name + "]]"
}

/**
 * Base class for enumerations of parameters for one document.
 */
abstract class TemplateParameters {
  
  private var parameters = List[TemplateParameter]()
  
  /**
   * Adds a new parameter to the list of parameters and returns
   * the new parameter.
   */
  def param(parameter: String): TemplateParameter = {
    val newParam = TemplateParameter(parameter)
    parameters = (newParam :: parameters).reverse
    newParam
  }
  
  /**
   * Returns all template parameters.
   */
  def allParameters(): List[TemplateParameter] = parameters
}
