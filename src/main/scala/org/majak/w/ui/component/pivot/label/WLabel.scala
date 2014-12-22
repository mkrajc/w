package org.majak.w.ui.component.pivot.label

import org.apache.pivot.wtk.{Label => PivotLabel, Component}

class WLabel extends PivotLabel {

  def this(text: String) = {
    this()
    setText(text)
  }

  override def installSkin(componentClass: Class[_ <: Component]): Unit = setSkin(new WLabelSkin)
}
