package org.majak.w.component.smallslide.live

import org.majak.w.component.presentation.PresentationPresenter
import org.majak.w.component.slide.SlideAdapter
import org.majak.w.ui.mvp.Presenter

class LiveSmallSlidePresenter(pp: PresentationPresenter) extends Presenter[LiveSmallSlideView] {

  override protected def onBind(v: LiveSmallSlideView) = {
    val slideAdapter = new SlideAdapter(v.slideView.snapshot.size)

    pp.addBindListener(presentationView => {
      logger.debug("Delayed binding [{}] with view [{}]", presentationView, this, None)
      presentationView.slideView.observable.subscribe(slideChange => view.slideView.apply(slideAdapter.adapt(slideChange.snapshot)))
      presentationView.addSizeChangedListener(dim => view.autoSizeSlideView(dim))
    })
  }

}
