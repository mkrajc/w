package org.majak.w.component.live.song

import org.majak.w.model.song.data.SongModel
import SongModel.SongPart
import org.majak.w.rx.{UiEvent, ObservableView}
import rx.lang.scala.Observable

case class SongPartSelected(songPart: SongPart) extends UiEvent

/**
 * Represent UI for song detail component
 */
trait SongDetailView extends ObservableView {
  private lazy val subjSongPartSelected = createUiEventSubject[SongPartSelected]

  def selectSongPart(songPart: SongPart) = subjSongPartSelected.onNext(SongPartSelected(songPart))

  def showSongName(name: String)

  def showSongParts(parts: List[SongPart])

  def clearSong()

  lazy val observable: Observable[SongPartSelected] = subjSongPartSelected


}
