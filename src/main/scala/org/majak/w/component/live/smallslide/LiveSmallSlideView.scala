package org.majak.w.component.live.smallslide

import org.majak.w.component.presentation.PresentationViewProvider

trait LiveSmallSlideView extends SmallSlideView {
  def addUiHandler(h: LiveSmallSlideUiHandler)

  def removeUiHandler(h: LiveSmallSlideUiHandler)
}

trait LiveSmallSlideUiHandler {
  def onStartPresentation(source: PresentationViewProvider)

  def onHidePresentation()
}


