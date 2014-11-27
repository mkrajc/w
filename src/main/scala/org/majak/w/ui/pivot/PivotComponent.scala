package org.majak.w.ui.pivot

import java.util.Locale

import org.apache.pivot.util.Resources
import org.apache.pivot.wtk.Label

/**
 * Implementation of [[PivotView]] that reads xmls from "/components/" directory and resource from
 * "i18n/Application[locale].json"
 *
 */
trait PivotComponent extends PivotView with Binding {
  override def xmlName = "/components/" + getClass.getSimpleName + ".xml"

  override protected val resources = new Resources(null, "i18n/Application", Locale.getDefault)

  override def bindView = bindUi

  override def asComponent = readXml.getOrElse(new Label("<<UNDEFINED>>"))

}
