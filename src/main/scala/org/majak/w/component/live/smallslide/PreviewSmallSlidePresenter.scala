package org.majak.w.component.live.smallslide

import org.majak.w.component.live.slide.TextContent
import org.majak.w.component.live.song.SongDetailView
import org.majak.w.component.presentation.PresentationPresenter
import org.majak.w.ui.mvp.Presenter

class PreviewSmallSlidePresenter(val pp: PresentationPresenter,
                                 val songDetailView: SongDetailView
                                  ) extends Presenter[PreviewSmallSlideView] {


  override protected def onBind(v: PreviewSmallSlideView) = {
    pp.addBindListener(presentationView => {
      logger.debug("Delayed binding [{}] with view [{}]", presentationView, this, None)
      presentationView.addSizeChangedListener(dim => view.autoSizeSlideView(dim))
    })

    songDetailView.addSongPartSelectedHandler(sp =>
      v.slideView.showContent(TextContent(sp.lines))
    )

    v.addConfirmListener(contents => {
      contents.foreach(c => pp.view.slideView.showContent(c))})
  }

}
