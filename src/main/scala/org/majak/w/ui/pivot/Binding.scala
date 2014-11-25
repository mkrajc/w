package org.majak.w.ui.pivot

import org.apache.pivot.beans.BXMLSerializer
import org.apache.pivot.util.Resources
import org.apache.pivot.wtk.Component

/**
 * Binds this instance with BXML file
 *
 * ==Overview==
 * Name of xml is get through [[Binding#xmlName]] method.
 *
 * All fields annotated with [[org.apache.pivot.beans.BXML]]
 * annotation are bound to instances from XML parsed object get from [[BXMLSerializer]].
 *
 * ==Example==
 * In class `Example.scala`
 * {{{
 * class Example with Binding[Label] {
 *
 *   //bind ui in constructor
 *   bindUi
 *
 *   @BXML
 *   var id: Label = _
 *
 *   override protected def onBind(v: View) = ...
 *
 *   ...
 * }}}
 *
 * In `example.xml`
 * {{{
 *  <Label xmlns:bxml="http://pivot.apache.org/bxml" xmlns="org.apache.pivot.wtk"  bxml:id="id" />
 * }}}
 *
 * @tparam T root component class in xml
 */
trait Binding[T <: Component] {

  /** default xml name - lowercased classname with '.xml' suffix **/
  private lazy val xmlNameDefault: String = getClass().getSimpleName().toLowerCase() + ".xml"



  protected lazy val serializer = new BXMLSerializer

  /** Root component object read from xml */
  protected lazy val root: T = serializer.readObject(getClass.getResource(xmlName), resources).asInstanceOf[T]

  /**
   * Reads XML and binds result with this instance.
   *
   * @see Binding
   */
  lazy val bindUi = {
    // first initialize root if not already
    root
    serializer.bind(this, getClass())
    onUiBind
  }

  /**
   * Returns XML filename used for reading and binding this object.
   */
  protected def xmlName = xmlNameDefault

  /**
   * Returns resoures for localization
   * @return resources object for localization
   */
  protected def  resources: Resources = null

  /**
   * Is called after binding.
   */
  protected def onUiBind = {}

}