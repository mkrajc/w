package org.majak.w.ui.pivot

import org.apache.pivot.wtk.Component
import org.majak.w.ui.pivot.Binding


trait PivotComponent[C <: Component] extends PivotView with Binding[C] {
  override def xmlName = "/components/" + getClass.getSimpleName + ".xml"

  val asComponent: C = root
}
