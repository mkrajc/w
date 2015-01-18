package org.majak.w.model.song.watchdog

import java.io.{FileInputStream, InputStream}

import org.majak.w.controller.watchdog.FileData
import org.majak.w.controller.watchdog.sync.DirectoryWatchDogSynchronizer
import org.majak.w.di.AppSettings
import org.majak.w.model.song.data.SongModel.Song
import org.majak.w.model.song.parser.SongParser
import org.majak.w.model.song.service.SongService
import org.majak.w.utils.Utils

class SongSynchronizer(val songWatchDog: SongDirectoryWatchDog, val songService: SongService, val parsers: List[SongParser])
  extends DirectoryWatchDogSynchronizer[Song, String](songWatchDog) with AppSettings {

  private var initialized = false

  override protected def create(fileData: FileData): Option[Song] = {
    val song = for {
      p <- parsers.find(_.ext == Utils.extension(fileData.path))
      sd <- p.parse(createInputStream(fileData))
    } yield Song(fileData.md5hex, sd.name, sd.parts)

    if(song.isEmpty){
      songWatchDog.ignoreFile(fileData)
    }

    song
  }

  protected def createInputStream(fileData: FileData): InputStream = {
    new FileInputStream(fileData.toFile)
  }

  override protected def createId(fileData: FileData): String = fileData.md5hex

  override def removeById(id: (String, FileData)): Boolean = {
    logger.info("Removing song: " + id._1)
    songService.findById(id._1).exists(songService.remove)
  }


  override def sync(): Unit = {
    if (!initialized) {
      logger.info("Refreshing/deleting index")
      songService.removeAll()
      initialized = true
    }
    super.sync()
  }

  override def add(song: Song): Unit = {
    logger.info("Adding song: " + song.name)
    songService.save(song)
  }

  override def objectWithIdExists(id: (String, FileData)): Boolean = {
    val exists = songService.findById(id._1).isDefined
    logger.info("Checking song: " + id._1 + "\t[" + Utils.okOrFailed(exists) + "]")
    exists
  }
}
