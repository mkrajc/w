package org.majak.w.service

import org.majak.w.model.Song

import scala.io.Source

class LocalSongService extends SongService{
  val source = Source.fromFile("songs.txt")

  def toSong(line: String): Song = {
    val sp = line.span(!_.isDigit)
    try {
      Song(sp._2.toInt, name = sp._1)
    } catch {
      case e: Exception => println(line); throw e
    }
  }

  val songs = source.getLines.toList map toSong

  override def getAllSongs(): List[Song] = songs

}
