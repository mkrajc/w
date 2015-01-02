package org.majak.w.component.smallslide

import org.apache.pivot.wtk.{Container, Component, ComponentListener}
import org.majak.w.component.slide.{SlideView, Slide}
import org.majak.w.ui.component.Size
import org.majak.w.ui.pivot.PivotComponent


abstract class SmallSlide extends PivotComponent with SmallSlideView {
  val slide = new Slide
  override val slideView: SlideView = slide
  var sourceSize: Option[Size] = None

  slide.getComponentListeners.add(new ComponentListener.Adapter {
    override def parentChanged(component: Component, previousParent: Container): Unit = {
      component.getParent.getComponentListeners.add(new ComponentListener.Adapter {

        override def sizeChanged(component: Component, previousWidth: Int, previousHeight: Int): Unit =
          sourceSize.map(_ => autoSizeSlideView())

      })
    }
  })

  protected def setupSlide(slide: Slide)

  override protected def onUiBind(): Unit = {
    setupSlide(slide)
  }

  override def autoSizeSlideView(size: Size): Unit = {
    sourceSize = size match {
      case Size(0, 0) => None
      case other => Some(other)
    }
    autoSizeSlideView()
  }

  protected def autoSizeSlideView(): Unit = {
    sourceSize match {
      case None => slide.setPreferredSize(0, 0)
      case Some(size) => autoscale()
    }

    def autoscale() = {
      var finalWidth = slide.getParent.getWidth.toFloat
      var finalHeight = slide.getParent.getHeight.toFloat

      sourceSize.map(size => {
        val sizeRatio = size.width.toFloat / size.height
        val slideRatio = finalWidth / finalHeight

        if (sizeRatio > slideRatio) finalHeight = finalWidth.toFloat / sizeRatio
        else finalWidth = finalHeight * sizeRatio
      })

      logger.trace(s"Autoscaled size [${finalWidth}x${finalHeight}] for slide [$slide]")
      slide.setPreferredSize(finalWidth.toInt, finalHeight.toInt)
      slide.repaint(true)
    }
  }
}
