package org.majak.w.component.live.song

import org.majak.w.ui.mvp.Presenter

class SongPanelPresenter(val songDetailPresenter: SongDetailPresenter)
  extends Presenter[SongPanelView] {

  override protected def onBind(v: SongPanelView) = {
    v.setPreviewSongDetailView(songDetailPresenter.view)
  }

}
