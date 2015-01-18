package org.majak.w.model.song.data

object SongModel {

  case class SongData(name: String, parts: List[SongPart])

  case class Song(id: String, name: String, parts: List[SongPart])

  case class SongPart(lines: List[String])

  case class SongListItem(id: String, name: String, hint: String)

}



