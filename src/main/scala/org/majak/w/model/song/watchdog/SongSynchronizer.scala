package org.majak.w.model.song.watchdog

import java.io.{FileInputStream, InputStream}

import org.majak.w.controller.ControllerSettings
import org.majak.w.controller.watchdog.FileData
import org.majak.w.controller.watchdog.sync.DirectoryWatchDogSynchronizer
import org.majak.w.model.song.data.SongModel.Song
import org.majak.w.model.song.parser.SongParser
import org.majak.w.model.song.service.SongService
import org.majak.w.utils.Utils


class SongSynchronizer(val songWatchDog: SongDirectoryWatchDog, val songService: SongService, val parsers: List[
  SongParser])
  extends DirectoryWatchDogSynchronizer[Song](songWatchDog) with ControllerSettings {

  override protected def create(fileData: FileData): Option[Song] = {
    val parser = parsers.find(_.ext == Utils.extension(fileData.path))
    parser.map(_.parse(createInputStream(fileData)))
  }

  protected def createInputStream(fileData: FileData): InputStream = {
    new FileInputStream(fileData.toFile)
  }

  override def remove(data: Song): Unit = {
    songService.remove(data)
  }

  override def add(data: Song): Unit = {
    songService.save(data)
  }

  override def isOk(fileData: FileData): Boolean = {
    songService.findByFileData(fileData).isDefined
  }
}
