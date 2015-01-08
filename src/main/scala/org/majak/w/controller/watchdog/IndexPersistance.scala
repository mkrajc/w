package org.majak.w.controller.watchdog

import org.majak.w.controller.watchdog.DirectoryWatchDog.IndexResult
import org.majak.w.controller.watchdog.IndexPersistance.{IndexProvider, IndexStore}

trait IndexPersistance extends WatchDog {
  val indexName = "dwd"

  def rescan(indexProvider: IndexProvider): Unit = {
    rescan(index(indexProvider))
  }

  def index(indexProvider: IndexProvider): Index = {
    indexProvider(indexName)
  }

  def scan(indexStore: IndexStore): IndexResult = {
    val r = scan()
    r match {
      case Left(index) => store(index, indexStore)
      case _ => ()
    }
    r
  }

  def store(index: Index, indexStore: IndexStore): Unit = {
    indexStore(indexName, index)
  }

}

object IndexPersistance {
  type IndexProvider = String => Index
  type IndexStore = (String, Index) => Unit
}
