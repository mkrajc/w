package org.majak.w.di

import java.io.File
import java.util.Date

import org.majak.w.controller.SongController
import org.majak.w.controller.watchdog.PersistentWatchDog.{IndexProvider, IndexStore}
import org.majak.w.controller.watchdog.WatchDog.IndexResult
import org.majak.w.controller.watchdog.{FileData, Index}
import org.majak.w.service.SongService
import org.mapdb.DBMaker

import scala.collection.JavaConversions._

trait Module {
  val songController = new SongController
  def songService: SongService = null

}

object Module {
  private lazy val db = DBMaker.newFileDB(new File("./index.dat")).closeOnJvmShutdown().make()

  def createIndexProvider: IndexProvider = {
    val provider = (s: String) => {
      println("create provider")
      if(db.getHashSet(s) == null) {
        db.createHashSet(s)
      }

      val fileDatas = db.getHashSet[FileData](s)
      Some(Index(fileDatas.toSet, new Date()))
    }

    provider
  }

  def createIndexStore: IndexStore = {
    (s:String, i: IndexResult) => {
      println("create store")
      if(db.getHashSet(s) == null) {
        db.createHashSet(s)
      }

      val fileDatas = db.getHashSet[FileData](s)
      fileDatas.addAll(i.get.fileData)

      db.commit()
    }
  }
}
