package org.majak.w.component.live.song

import org.majak.w.model.song.controller.SongController
import org.majak.w.model.song.data.SongModel.Song
import org.majak.w.model.song.rx.{SongEvent, SongSelected}
import org.majak.w.ui.mvp.Presenter

/**
 * Represents whole song on screen
 */
class SongDetailPresenter(songController: SongController) extends Presenter[SongDetailView] {

  def song(s: Song) = {
    view.showSongName(s.name)
    view.showSongParts(s.parts)
  }

  override protected def onBind(v: SongDetailView): Unit = {
    songController.observable.subscribe(onNext = e => handleSongEvent(e))
  }

  private def handleSongEvent(event: SongEvent) = {
    event match {
      case SongSelected(s) => showSong(s)
      case _ => ()
    }
  }

  private def showSong(song: Song): Unit = {
    view.clearSong()
    view.showSongName(song.name)
    view.showSongParts(song.parts)
  }
}
