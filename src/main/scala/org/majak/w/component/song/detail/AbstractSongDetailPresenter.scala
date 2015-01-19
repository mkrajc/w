package org.majak.w.component.song.detail

import org.majak.w.model.song.data.SongModel.Song
import org.majak.w.model.song.rx.{SongEvent, SongSelected, SongsRefreshed}
import org.majak.w.ui.mvp.Presenter

/**
  * Represents whole song on screen
  */
class AbstractSongDetailPresenter() extends Presenter[SongDetailView] {
   protected var song: Option[Song] = None

  protected def refreshSongView(songs: List[Song]) = {
     if (song.isDefined) {
       val found = songs.find(_.id == song.get.id)
       if (found.isDefined) {
         setSong(found.get)
       } else {
         clear()
       }
     }
   }

  protected def handleSongEvent(event: SongEvent) = {
     event match {
       case SongSelected(s) => setSong(s)
       case SongsRefreshed(songs) => refreshSongView(songs)
     }
   }

  protected def clear() = {
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
