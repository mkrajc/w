package org.majak.w.controller.watchdog

import java.io.File

import org.majak.w.controller.watchdog.PersistentWatchDog.{IndexProvider, IndexStore}
import org.majak.w.controller.watchdog.WatchDog.IndexResult
import org.majak.w.di.Module

trait PersistentWatchDog extends WatchDog {
  val indexName: String
  val indexFile: File

  lazy val (indexProvider: IndexProvider, indexStore: IndexStore) = Module.createIndex(indexFile)

  def refresh(): Unit = {
    val idx = index()
    val newIdx = scan()

    processIndex(idx, newIdx)
  }

  def index(): IndexResult = {
    indexProvider(indexName)
  }

  def store(index: IndexResult): Unit = {
    indexStore(indexName, index)
  }

  /**
   * Process indices
   */
  def processIndex(currentIndex: IndexResult, previousIndex: IndexResult): Unit

}

object PersistentWatchDog {
  type IndexProvider = String => IndexResult
  type IndexStore = (String, IndexResult) => Unit
}
