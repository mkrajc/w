package org.majak.w.component.slide

import org.majak.w.ui.component.Size
import org.slf4j.LoggerFactory


class SlideAdapter(size: => Size ) {

  val logger = LoggerFactory.getLogger(getClass)

  def adapt(s: SlideSnapshot): SlideSnapshot = {
    SlideSnapshot(s.front: Front, adaptBack(s.back: Back), adaptFontSettings(s.fontSettings, s.size), size,
      s.horizontalAlignment, s.verticalAlignment)
  }

  private def adaptFontSettings(fontSettings: FontSettings, slideSize: Size): FontSettings = {
    val ratio: Float = slideSize.height.toFloat / fontSettings.size
    val adaptedSize = math.round(size.height / ratio)
    logger.trace(s"$this Font size adapted to: $adaptedSize [orig:${fontSettings.size}]")
    FontSettings(adaptedSize.toInt, fontSettings.family)
  }

  private def adaptBack(back: Back): Back = {
    back match {
      case t: ThumbnailContent =>
        logger.trace(s"$this Converting thumbnail to image " + t.thumb.source.name)
        ImageContent(t.thumb.loadImage())
      case _ => back
    }
  }
}
