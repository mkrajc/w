package org.majak.w.component.live.smallslide

import org.majak.w.component.live.slide.{Content, SlideListener}
import org.majak.w.component.presentation.PresentationViewProvider
import org.majak.w.ui.mvp.View

trait SmallSlideView extends View {
  def showContent(content: Content)
}

trait LiveSmallSlideView extends SmallSlideView with SlideListener {
  def addUiHandler(h: LiveSmallSlideUiHandler)
}

trait LiveSmallSlideUiHandler {
  def onStartPresentation(source: PresentationViewProvider)

  def onHidePresentation()
}

