package org.majak.w.component.live.screen

import org.majak.w.component.live.smallslide.LiveSmallSlidePresenter
import org.majak.w.ui.mvp.Presenter


class LiveScreenPresenter(v: LiveScreenView, smSlide: LiveSmallSlidePresenter) extends Presenter[LiveScreenView](v) {
  override protected def onBind(v: LiveScreenView) = {
    view.setLiveSmallSlideView(smSlide.view)
  }
}
