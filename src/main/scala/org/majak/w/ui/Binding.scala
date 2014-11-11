package org.majak.w.ui

import org.apache.pivot.beans.BXMLSerializer

trait Binding {

  lazy val serializer = new BXMLSerializer

  def xmlName: String = getClass().getSimpleName().toLowerCase() + ".xml"

  def parse[R](root: Class[R]): R = serializer.readObject(getClass(), xmlName).asInstanceOf[R]

  def bind[R](root: Class[R]): R = {
    val ret = parse(root)
    serializer.bind(this, getClass())
    ret
  }

}