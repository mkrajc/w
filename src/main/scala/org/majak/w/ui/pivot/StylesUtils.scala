package org.majak.w.ui.pivot

import java.awt.{Color, Font}

import org.apache.pivot.wtk.{Component, HorizontalAlignment, VerticalAlignment}
import org.majak.w.utils.Utils


object StylesUtils {
  def applyPadding(comp: Component, leftRight: Int, topBottom: Int): Unit = {
    applyPadding(comp, leftRight, leftRight, topBottom, topBottom)
  }

  def applyPadding(comp: Component, left: Int, right: Int, top: Int, bottom: Int): Unit = {
    comp.getStyles.put("padding", s"{top:$top,bottom:$bottom,left:$left,right:$right}}")
  }

  def colorToHex(color: Color): String = {
    "#" + Integer.toHexString(color.getRGB).substring(2)
  }

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

  def setFontFamily(comp: Component, family: String): Unit = {
    val fontOption = Utils.nullAsOption[Font](comp.getStyles.get("font"))
    val f = fontOption.getOrElse(defaultFont)
    comp.getStyles.put("font", new Font(family, f.getStyle, f.getSize))
  }

  val defaultFont: Font = new Font("Arial", Font.BOLD, 12)

}
