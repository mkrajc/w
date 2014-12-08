package org.majak.w.component.presentation

import org.apache.pivot.wtk.Dimensions
import org.majak.w.component.live.slide.{Content, SlideListener}
import org.majak.w.ui.mvp.View

trait PresentationView extends View with SlideListener {

  type SizeChangedListener = (Dimensions) => Unit

  def show()
  def hide()

  def addSizeChangedListener(l: SizeChangedListener)
  def removeSizeChangedListener(l: SizeChangedListener)

  def addSlideListener(slideListener: SlideListener)

  def showSlide(content: Content)

}


