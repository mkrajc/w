package org.majak.w.component.slide

import org.majak.w.ui.component.Size
import org.slf4j.LoggerFactory


class SlideAdapter(size: => Size) {

  val logger = LoggerFactory.getLogger(getClass)

  def adapt(s: SlideSnapshot): SlideSnapshot = {
    SlideSnapshot(s.front: Front, adaptBack(s.back: Back), adaptSettings(s.settings, s.size), size)
  }

  private def adaptSettings(settings: Settings, slideSize: Size): Settings = {
    val fontSettings = settings.text.font
    val ratio: Float = slideSize.height.toFloat / fontSettings.size
    val adaptedSize = math.round(size.height / ratio)
    logger.trace(s"$this Font size adapted to: $adaptedSize [orig:${fontSettings.size}]")
    settings.updateFontSettings(fontSettings.setSize(adaptedSize.toInt))
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
