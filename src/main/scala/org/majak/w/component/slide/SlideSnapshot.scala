package org.majak.w.component.slide

import java.awt.Color

import org.apache.pivot.wtk.{HorizontalAlignment, VerticalAlignment}
import org.majak.w.ui.component.Size

case class Settings(text: TextSettings, image: ImageSettings) {
  def updateTextSettings(setting: TextSettings) : Settings = {
    Settings(setting, image)
  }

  def updateFontSettings(setting: FontSettings) : Settings = {
    Settings(text.setFontSettings(setting), image)
  }

  def updateImageSettings(setting: ImageSettings) : Settings = {
    Settings(text, setting)
  }
}

case class TextSettings(font: FontSettings, horizontalAlignment: HorizontalAlignment, verticalAlignment:
VerticalAlignment) {
  def setFontSettings(s: FontSettings): TextSettings = TextSettings(s, horizontalAlignment, verticalAlignment)
  def setVerticalAlign(va: VerticalAlignment): TextSettings = TextSettings(font, horizontalAlignment, va)
  def setHorizontalAlign(ha: HorizontalAlignment): TextSettings = TextSettings(font, ha, verticalAlignment)
}

case class FontSettings(size: Int, family: String) {
  def setSize(s: Int): FontSettings = FontSettings(s, family)

  def setFamily(fam: String): FontSettings = FontSettings(size, fam)
}

case class ImageSettings(background: Color, stretch: Boolean) {
  def setBackground(color: Color): ImageSettings = ImageSettings(color, stretch)

  def setStretch(stretchArg: Boolean): ImageSettings = ImageSettings(background, stretchArg)
}

case class SlideSnapshot(front: Front, back: Back, settings: Settings, size: Size)
