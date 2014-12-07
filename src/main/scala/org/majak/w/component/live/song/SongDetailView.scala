package org.majak.w.component.live.song

import org.majak.w.model.SongModel.SongPart
import org.majak.w.ui.mvp.View

/**
 * Represent UI for song detail component
 */
trait SongDetailView extends View {

  type SongPartSelectedHandler = SongPart => Unit

  def showSongName(name: String)

  def showSongParts(parts: List[SongPart])

  def addSongPartSelectedHandler(h: SongPartSelectedHandler)

  def clearSong()

}
