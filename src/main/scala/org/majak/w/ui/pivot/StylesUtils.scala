package org.majak.w.ui.pivot

import org.apache.pivot.wtk.{VerticalAlignment, Component, HorizontalAlignment}


object StylesUtils {

  def centerHorizontal(comp: Component) = {
    applyHorizontalAlignement(comp, HorizontalAlignment.CENTER)
  }

  def centerVertical(comp: Component) = {
    applyVerticalAlignement(comp, VerticalAlignment.CENTER)
  }

  def setBackground(comp: Component, color: String) = {
    comp.getStyles.put("backgroundColor", color)
  }

  def applyHorizontalAlignement(comp: Component, align: HorizontalAlignment): Unit = {
    comp.getStyles.put("horizontalAlignment", align)
  }

  def applyVerticalAlignement(comp: Component, align: VerticalAlignment): Unit = {
    comp.getStyles.put("verticalAlignment", align)
  }
}
