package org.majak.w.service

import org.majak.w.controller.watchdog.FileData
import org.majak.w.model.song.data.SongModel.Song
import org.majak.w.model.song.service.SongService

import scala.io.Source

class LocalSongService extends SongService{
  val source = Source.fromFile("src/test/resources/songs.txt")

  def toSong(line: String): Song = {
    val sp = line.span(!_.isDigit)
    try
      Song(sp._2, name = sp._1, Nil)
    catch {
      case e: Exception => println(line); throw e
    }
  }

  val songs = source.getLines.toList map toSong

  override def remove(song: Song): Boolean = ???

  override def save(song: Song): Song = ???

  override def findByFileData(fileData: FileData): Option[Song] = None

  override def findById(id: String): Option[Song] = ???
}
