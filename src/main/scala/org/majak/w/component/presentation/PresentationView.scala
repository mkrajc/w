package org.majak.w.component.presentation

import org.majak.w.component.live.slide.SlideView
import org.majak.w.ui.component.Size
import org.majak.w.ui.mvp.View

trait PresentationView extends View {

  type SizeChangedListener = (Size) => Unit

  def show()

  def hide()

  def slideView: SlideView

  def addSizeChangedListener(l: SizeChangedListener)

  def removeSizeChangedListener(l: SizeChangedListener)

}


