package org.majak.w.controller.watchdog

import java.io.File
import java.util.Date

import org.majak.w.controller.watchdog.WatchDog.IndexResult

case class Index(fileData: Set[FileData], createdAt: Date)

trait WatchDog {
  /**
   * Gets file that is watched.
   */
  def file(): File

  /**
     * Scans file
   * @return result
   */
  def scan(): IndexResult

  def rescan(index: IndexResult): IndexResult

}

object WatchDog {

  type IndexResult = Option[Index]
}
