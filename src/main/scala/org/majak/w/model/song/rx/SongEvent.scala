package org.majak.w.model.song.rx

import org.majak.w.model.song.data.SongModel.Song
import org.majak.w.rx.Event

sealed trait SongEvent extends Event

case class SongsRefreshed(songs: List[Song]) extends SongEvent

case class SongSelected(song: Song) extends SongEvent
