package org.majak.w.model.song

import org.majak.w.component.live.song._
import org.majak.w.component.songlist.{SongListComponent, SongListPresenter, SongListView}
import org.majak.w.di.UiModule

trait SongUiModule extends UiModule with SongModule {

  lazy val songDetailView = new SongDetail
  lazy val songDetailPresenter = doBind[SongDetailView, SongDetailPresenter](new SongDetailPresenter(songController),
    songDetailView)

  lazy val songListPresenter =
    doBind[SongListView, SongListPresenter](new SongListPresenter(songController), new SongListComponent)

  lazy val songPanelPresenter = doBind[SongPanelView, SongPanelPresenter](
    new SongPanelPresenter(songDetailPresenter, songListPresenter),
    new SongPanel)


}
