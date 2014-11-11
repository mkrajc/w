package org.majak.w.ui

import org.apache.pivot.beans.BXMLSerializer

/**
 * Helps binds BXML files into instances of [[org.majak.w.ui.Binding]] trait
 *
 * @author martin.krajc
 */
trait Binding[T] {

  protected lazy val serializer = new BXMLSerializer

  protected lazy val xmlName: String = getClass().getSimpleName().toLowerCase() + ".xml"
  
  protected lazy val root: T = serializer.readObject(getClass(), xmlName).asInstanceOf[T]
  
  lazy val bind = {
    // first initialize root if not already
    root
    serializer.bind(this, getClass())
  }

}