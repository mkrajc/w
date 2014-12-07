package org.majak.w.component.presentation

import org.majak.w.component.live.slide.{Content, SlideListener}
import org.majak.w.ui.mvp.View

trait PresentationView extends View with SlideListener {

  def show()

  def hide()

  def addSlideListener(slideListener: SlideListener)

  def showSlide(content: Content)

}
