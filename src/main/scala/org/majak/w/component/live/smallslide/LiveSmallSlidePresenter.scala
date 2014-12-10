package org.majak.w.component.live.smallslide

import org.majak.w.component.presentation.PresentationPresenter
import org.majak.w.ui.mvp.Presenter

class LiveSmallSlidePresenter(pp: PresentationPresenter) extends Presenter[LiveSmallSlideView] {

  override protected def onBind(v: LiveSmallSlideView) = {
    v.addUiHandler(pp)
    pp.addBindListener(presentationView => {
      logger.debug("Delayed binding [{}] with view [{}]", presentationView, this, None)
      presentationView.slideView.addSlideContentListener(content => view.slideView.showContent(content))
      presentationView.addSizeChangedListener(dim => view.autoSizeSlideView(dim))
    })
  }

}
