package org.majak.w.ui.pivot

import java.awt.Font

import org.apache.pivot.wtk.{VerticalAlignment, Component, HorizontalAlignment}
import org.majak.w.utils.Utils


object StylesUtils {

  def centerHorizontal(comp: Component) = {
    applyHorizontalAlignement(comp, HorizontalAlignment.CENTER)
  }

  def centerVertical(comp: Component) = {
    applyVerticalAlignement(comp, VerticalAlignment.CENTER)
  }

  def setColor(comp: Component, color: String) = {
    comp.getStyles.put("color", color)
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

  def setFontSize(comp: Component, size: Int): Unit = {
    val fontOption = Utils.nullAsOption[Font](comp.getStyles.get("font"))
    val f = fontOption.getOrElse(defaultFont)
    comp.getStyles.put("font", new Font(f.getFamily, f.getStyle, size))
  }

  val defaultFont: Font = new Font("Arial", Font.BOLD, 12)

}
