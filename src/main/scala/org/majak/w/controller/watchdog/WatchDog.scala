package org.majak.w.controller.watchdog

import org.majak.w.controller.watchdog.DirectoryWatchDog._


trait WatchDog {
  def scan(): IndexResult
  def rescan(lastIndex: Index): IndexResult
}
