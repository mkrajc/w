package org.majak.w.component.presentation

import org.majak.w.component.live.slide.{Content, SlideListener}
import org.majak.w.component.live.smallslide.LiveSmallSlideUiHandler
import org.majak.w.ui.mvp.Presenter

/**
 * Managed presentation screen
 */
class PresentationPresenter(sl: SlideListener) extends Presenter[PresentationView] with LiveSmallSlideUiHandler {

  override def onHidePresentation = {
    if (bound) {
      view.hide()
      unbind
    }
  }

  override def onStartPresentation(source: PresentationViewProvider) = {
    if (!bound) {
      this bind source.create
      view.addSlideListener(sl)
      view.show()
    }
  }

  def showSlide(content: Content) =
    if (bound) {
      view.showSlide(content)
    }

}
