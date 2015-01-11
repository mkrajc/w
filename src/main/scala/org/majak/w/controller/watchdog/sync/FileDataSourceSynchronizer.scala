package org.majak.w.controller.watchdog.sync

import org.majak.w.controller.watchdog.{DirectoryWatchDog, FileData}

trait FileDataSource {
  def list(): Set[FileData]
}

abstract class FileDataSourceSynchronizer[T](val source: FileDataSource) {

  protected def create(fileData: FileData): T

  def add(data: T): Unit

  def remove(fileData: FileData): Unit

  def isOk(fileData: FileData): Boolean

  def sync(): Unit = {
    val toBeAdded = source.list().filterNot(isOk)
    toBeAdded.map(f => add(create(f)))
  }
}

abstract class DirectoryWatchDogSynchronizer[T](val wd: DirectoryWatchDog) extends FileDataSourceSynchronizer[T](wd) {

  override def sync(): Unit = {
    wd.refresh()
    super.sync()
  }
}
