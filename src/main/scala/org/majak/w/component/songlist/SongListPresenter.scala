package org.majak.w.component.songlist

import org.majak.w.component.songlist.pivot.SongListComponent
import org.majak.w.component.songlist.view.SongListView
import org.majak.w.controller.{SongController, SongIndexChangeListener}
import org.majak.w.model.SongListItem
import org.majak.w.service.SongService
import org.majak.w.ui.mvp.{Presenter, View}

class SongListPresenter(songController: SongController,
                        songService: SongService)
  extends Presenter[SongListView] with SongIndexChangeListener {

  override protected def createViewImpl: SongListView = new SongListComponent

  override protected def onBind(v: View) = songController += this

  override def onSongIndexChanged: Unit = view.showData(songService.getAllSongs map (s => SongListItem(s.id, s.name, s.name)))
}