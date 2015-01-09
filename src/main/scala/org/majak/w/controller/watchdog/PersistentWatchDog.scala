package org.majak.w.controller.watchdog

import org.majak.w.controller.watchdog.PersistentWatchDog.{IndexStore, IndexProvider}
import org.majak.w.controller.watchdog.WatchDog.IndexResult

trait PersistentWatchDog extends WatchDog {
  val indexName: String
  val indexProvider: IndexProvider
  val indexStore: IndexStore


  def refresh(): Unit = {
    val idx = index()
    val newIdx = scan()

    notifyChanges(idx, newIdx)
  }

  def index(): IndexResult = {
    indexProvider(indexName)
  }

  def store(index: IndexResult, indexStore: IndexStore): Unit = {
    indexStore(indexName, index)
  }

}

object PersistentWatchDog {
  type IndexProvider = String => IndexResult
  type IndexStore = (String, IndexResult) => Unit
}
