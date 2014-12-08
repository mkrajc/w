package org.majak.w.component.live.song

import org.majak.w.component.live.smallslide.LiveSmallSlidePresenter
import org.majak.w.ui.mvp.Presenter

class SongPanelPresenter(val liveSmallSlidePresenter: LiveSmallSlidePresenter,
                         val songDetailPresenter: SongDetailPresenter)
  extends Presenter[SongPanelView] {

  override protected def onBind(v: SongPanelView) = {
    v.setLiveSmallSlideView(liveSmallSlidePresenter.view)
    v.setPreviewSongDetailView(songDetailPresenter.view)
  }

}
