package org.majak.w.component.live.song

import org.majak.w.model.song.controller.SongController
import org.majak.w.model.song.data.SongModel.Song
import org.majak.w.model.song.rx.{SongsRefreshed, SongEvent, SongSelected}
import org.majak.w.ui.mvp.Presenter

/**
 * Represents whole song on screen
 */
class SongDetailPresenter(songController: SongController) extends Presenter[SongDetailView] {
  private var song: Option[Song] = None

  override protected def onBind(v: SongDetailView): Unit = {
    songController.observable.subscribe(onNext = e => handleSongEvent(e))
  }

  private def refreshSongView(songs: List[Song]) = {
    if (song.isDefined) {
      val found = songs.find(_.id == song.get.id)
      if (found.isDefined) {
        setSong(found.get)
      } else {
        clear()
      }
    }
  }

  private def handleSongEvent(event: SongEvent) = {
    event match {
      case SongSelected(s) => setSong(s)
      case SongsRefreshed(songs) => refreshSongView(songs)
    }
  }

  private def clear() = {
    view.clearSong()
    this.song = None
  }

  private def setSong(song: Song): Unit = {
    this.song = Some(song)
    view.clearSong()
    view.showSongName(song.name)
    view.showSongParts(song.parts)
  }
}
