package org.majak.w.component.live.smallslide

import org.majak.w.component.presentation.PresentationPresenter
import org.majak.w.ui.mvp.Presenter

class PreviewSmallSlidePresenter(val pp: PresentationPresenter
                                  ) extends Presenter[PreviewSmallSlideView] {


  override protected def onBind(v: PreviewSmallSlideView) = {
    pp.addBindListener(presentationView => {
      logger.debug("Delayed binding [{}] with view [{}]", presentationView, this, None)
      presentationView.addSizeChangedListener(dim => view.autoSizeSlideView(dim))
    })
  }

}
