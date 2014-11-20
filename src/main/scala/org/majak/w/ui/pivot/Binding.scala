package org.majak.w.ui.pivot

import org.apache.pivot.beans.BXMLSerializer

/**
 * Helps binds BXML files into instances of [[Binding]] trait
 *
 * @author martin.krajc
 */
trait Binding[T] {

  private lazy val xmlNameDefault: String = getClass().getSimpleName().toLowerCase() + ".xml"

  protected lazy val serializer = new BXMLSerializer

  protected lazy val root: T = serializer.readObject(getClass(), xmlName).asInstanceOf[T]

  lazy val bindUi = {
    // first initialize root if not already
    root
    serializer.bind(this, getClass())
    onUiBind
  }

  /**
   *  xml name
   */
  protected def xmlName = xmlNameDefault

  /**
   * hook called after binding
   */
  protected def onUiBind = {}

}