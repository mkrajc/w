package org.majak.w.component.live.song

import org.majak.w.component.song.detail.{SongDetailLivePresenter, SongDetailPresenter}
import org.majak.w.component.songlist.SongListPresenter
import org.majak.w.ui.mvp.Presenter

class SongPanelPresenter(val songDetailPresenter: SongDetailPresenter,
                         val songLiveDetailPresenter: SongDetailLivePresenter,
                         val songListPresenter: SongListPresenter)
  extends Presenter[SongPanelView] {

  override protected def onBind(v: SongPanelView) = {
    v.setPreviewSongDetailView(songDetailPresenter.view)
    v.setLiveSongDetailView(songLiveDetailPresenter.view)
    v.setSongListView(songListPresenter.view)
  }

}
