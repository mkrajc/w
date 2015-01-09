package org.majak.w.controller.watchdog

import java.io.File
import java.util.Date

import org.majak.w.controller.watchdog.WatchDog.IndexResult
import org.majak.w.rx.ObservableObject

case class Index(fileData: Set[FileData], createdAt: Date)

trait WatchDog extends ObservableObject {
  /**
   * Gets file that is watched.
   */
  def file(): File

  /**
     * Scans file
   * @return result
   */
  def scan(): IndexResult

  def rescan(index: IndexResult): IndexResult = {
      val newIdx = scan()
      notifyChanges(index, newIdx)
      newIdx
  }

  /**
   * Notify changed items
   */
  def notifyChanges(currentIndex: IndexResult, previousIndex: IndexResult)
}

object WatchDog {

  type IndexResult = Option[Index]
}
