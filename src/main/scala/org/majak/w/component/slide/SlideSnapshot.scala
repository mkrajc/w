package org.majak.w.component.slide

import org.apache.pivot.wtk.{VerticalAlignment, HorizontalAlignment}
import org.majak.w.ui.component.Size

case class FontSettings(size: Int, family: String) {
  def setSize(s: Int): FontSettings = FontSettings(s, family)
  def setFamily(fam: String): FontSettings = FontSettings(size, fam)
}

case class SlideSnapshot(front: Front, back: Back, fontSettings: FontSettings, size: Size,
                          horizontalAlignment: HorizontalAlignment, verticalAlignment: VerticalAlignment)
