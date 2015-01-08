package org.majak.w.di

import java.io.File
import java.util.Date

import org.majak.w.controller.SongController
import org.majak.w.controller.watchdog.{Index, FileData}
import org.majak.w.controller.watchdog.IndexPersistance.{IndexStore, IndexProvider}
import org.majak.w.service.SongService
import org.mapdb.DBMaker
import scala.collection.JavaConversions._

trait Module {
  val songController = new SongController
  def songService: SongService = null

  lazy val db = DBMaker.newFileDB(new File("./index.dat")).closeOnJvmShutdown().make()

  def createIndexProvider: IndexProvider = {
    s: String => {
      println("create provider")
      if(db.getHashSet(s) == null) {
        db.createHashSet(s)
      }

      val fileDatas = db.getHashSet[FileData](s)

      Index(fileDatas.toSet, new Date())
    }
  }

  def createIndexStore: IndexStore = {
    (s:String, i: Index) => {
      println("create store")
      if(db.getHashSet(s) == null) {
        db.createHashSet(s)
      }

      val fileDatas = db.getHashSet[FileData](s)
      fileDatas.addAll(i.fileData)

      db.commit()
    }
  }

}
