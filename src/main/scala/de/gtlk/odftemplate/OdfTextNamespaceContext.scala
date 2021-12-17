package de.gtlk.odftemplate

import org.odftoolkit.odfdom.dom.OdfDocumentNamespace

import java.util
import javax.xml.namespace.NamespaceContext

object OdfTextNamespaceContext extends NamespaceContext {

  override def getNamespaceURI(prefix: String): String = {
    OdfDocumentNamespace.TEXT.getUri
  }

  override def getPrefix(namespaceURI: String): String = {
    // Not needed for XPath
    throw new UnsupportedOperationException()
  }

  override def getPrefixes(namespaceURI: String): util.Iterator[String] = {
    // Not needed for XPath
    throw new UnsupportedOperationException()
  }
}
