package org.majak.w.component.live.smallslide

import org.majak.w.component.presentation.PresentationViewProvider
import org.majak.w.ui.mvp.View

trait SmallSlideView extends View {

}

trait LiveSmallSlideView extends View {
  def addUiHandler(h: LiveSmallSlideUiHandler)
}

trait LiveSmallSlideUiHandler {
  def onStartPresentation(source: PresentationViewProvider)
  def onHidePresentation
}

