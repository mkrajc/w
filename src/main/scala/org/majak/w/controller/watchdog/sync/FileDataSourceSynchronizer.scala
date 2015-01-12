package org.majak.w.controller.watchdog.sync

import org.majak.w.controller.watchdog._
import org.slf4j.LoggerFactory
import rx.lang.scala.Observer

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

abstract class DirectoryWatchDogSynchronizer[T](val wd: DirectoryWatchDog) extends FileDataSourceSynchronizer[T](wd)
with Observer[WatchDogEvent] {
  private val logger = LoggerFactory.getLogger(getClass)

  wd.observable.subscribe(this)

  override def sync(): Unit = {
    logger.info("Syncing directory: " + wd.file().getAbsolutePath)
    val start = System.currentTimeMillis()
    wd.refresh()
    super.sync()
    logger.info("Syncing directory: " + wd.file().getAbsolutePath + s" DONE (${System.currentTimeMillis() - start}ms)")
  }

  override def onNext(event: WatchDogEvent): Unit = {
    event match {
      case FileAdded(data) => add(create(data))
      case FileRemoved(data) => remove(data)
      case FileChanged(from, data) => {
        remove(from)
        add(create(data))
      }
    }
  }
}
