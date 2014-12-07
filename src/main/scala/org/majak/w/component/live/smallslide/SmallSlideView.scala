package org.majak.w.component.live.smallslide

import org.majak.w.component.live.slide.SlideListener
import org.majak.w.component.presentation.PresentationViewProvider
import org.majak.w.ui.mvp.View

trait SmallSlideView extends View

trait LiveSmallSlideView extends View with SlideListener {
  def addUiHandler(h: LiveSmallSlideUiHandler)
}

trait LiveSmallSlideUiHandler {
  def onStartPresentation(source: PresentationViewProvider)

  def onHidePresentation()
}

