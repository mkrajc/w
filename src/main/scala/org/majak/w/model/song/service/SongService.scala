package org.majak.w.model.song.service

import org.majak.w.controller.watchdog.FileData
import org.majak.w.model.song.data.SongModel.Song

trait SongService {
  def remove(song: Song): Boolean

  def save(song: Song): Song

  def findByFileData(fileData: FileData): Option[Song]

  def songs: List[Song]

  def findById(id: String): Option[Song]
}
