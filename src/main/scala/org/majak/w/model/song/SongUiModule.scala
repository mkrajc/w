package org.majak.w.model.song

import org.majak.w.component.live.song._
import org.majak.w.component.song.detail.{SongDetailLivePresenter, SongDetailView, SongDetailPresenter, SongDetail}
import org.majak.w.component.songlist.{SongListComponent, SongListPresenter, SongListView}
import org.majak.w.di.UiModule

trait SongUiModule extends UiModule with SongModule {

  lazy val songDetailPresenter = doBind[SongDetailView, SongDetailPresenter](new SongDetailPresenter(songController),
    new SongDetail)

  lazy val songLiveDetailPresenter = doBind[SongDetailView, SongDetailLivePresenter](new SongDetailLivePresenter,
    new SongDetail)

  lazy val songListPresenter =
    doBind[SongListView, SongListPresenter](new SongListPresenter(songController), new SongListComponent)

  lazy val songPanelPresenter = doBind[SongPanelView, SongPanelPresenter](
    new SongPanelPresenter(songDetailPresenter, songLiveDetailPresenter, songListPresenter),
    new SongPanel)


}
