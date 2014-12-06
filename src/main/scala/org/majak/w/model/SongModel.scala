package org.majak.w.model

object SongModel {

  case class Song(id: Int, name: String, parts: List[SongPart])

  case class SongPart(lines: List[String])

  case class SongListItem(id: Int, name: String, hint: String)

}



