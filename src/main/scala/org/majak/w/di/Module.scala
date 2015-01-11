package org.majak.w.di

import java.io.File

import org.majak.w.controller.watchdog.image.ImageDirectoryWatchDog
import org.majak.w.controller.{ControllerSettings, SongController}
import org.majak.w.controller.watchdog.PersistentWatchDog.{IndexProvider, IndexStore}
import org.majak.w.controller.watchdog.WatchDog.IndexResult
import org.majak.w.controller.watchdog.Index
import org.majak.w.service.SongService
import org.mapdb.{DB, DBMaker}

trait Module extends ControllerSettings{
  val songController = new SongController

  def songService: SongService = null

  lazy val imageWatchDog = new ImageDirectoryWatchDog(new File(imageDir))
}

object Module {



  def createIndex(indexFile: File): (IndexProvider, IndexStore) = {
    val db = DBMaker.newFileDB(indexFile).closeOnJvmShutdown().make()
    (createIndexProvider(db), createIndexStore(db))
  }

  def createIndexProvider(db: DB): IndexProvider = {

    val provider = (s: String) => {
      val store = db.getHashMap[String, Index]("test")
      val v = store.get(s)
      Option[Index](v)
    }

    provider
  }

  def createIndexStore(db: DB): IndexStore = {
    (s: String, i: IndexResult) => {
      val store = db.getHashMap[String, Index]("test")

      i.foreach(store.put(s, _))

      db.commit()
    }
  }
}
