package org.majak.w.ui.pivot

import java.util.Locale

import org.apache.pivot.util.Resources
import org.apache.pivot.wtk.Component

/**
 * Implementation of [[PivotView]]
 *
 * @tparam C
 */
trait PivotComponent[C <: Component] extends PivotView with Binding[C] {
  override def xmlName = "/components/" + getClass.getSimpleName + ".xml"
  override protected val resources =  new Resources(null, "i18n/Application", Locale.getDefault)
  override def bindView = bindUi

  val asComponent: C = root


}
