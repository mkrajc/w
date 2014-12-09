package org.majak.w.component.presentation

import org.majak.w.component.live.smallslide.LiveSmallSlideUiHandler
import org.majak.w.ui.mvp.Presenter

/**
 * Managed presentation screen
 */
class PresentationPresenter extends Presenter[PresentationView] with LiveSmallSlideUiHandler {

  override def onHidePresentation = {
    if (bound) {
      view.hide()
      unbind
    }
  }

  override def onStartPresentation(source: PresentationViewProvider) = {
    if (!bound) {
      this bind source.create
      view.show()
    }
  }

/**  def showxSlide(content: Content) =
    if (bound) {
      view.slideView.showContent(content)
    } **/

}
