package org.majak.w.controller.watchdog.sync

import org.majak.w.controller.watchdog._
import org.slf4j.LoggerFactory
import rx.lang.scala.Observer

trait FileDataSource {
  def list(): Set[FileData]
}

abstract class FileDataSourceSynchronizer[T, ID](val source: FileDataSource) {
  val logger = LoggerFactory.getLogger(getClass)

  protected def cast(fileData: FileData): Option[T]

  protected def createId(fileData: FileData): ID

  def add(data: T): Unit

  def removeById(id: (ID, FileData)): Boolean

  def objectWithIdExists(id: (ID, FileData)): Boolean

  def ensureExisting(ids: Set[(ID, FileData)]): Unit = {
    ids.foreach(p => {
      if (!objectWithIdExists(p)) {
        cast(p._2).foreach(add)
      }
    })
  }

  def sync(): Unit = {
    val list = source.list()
    val ids = list.map(fd => (createId(fd), fd))
    ensureExisting(ids)
  }
}

abstract class DirectoryWatchDogSynchronizer[T, ID](val wd: DirectoryWatchDog) extends FileDataSourceSynchronizer[T, ID](wd)
with Observer[WatchDogEvent] {

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
      case FileAdded(data) => cast(data).foreach(add)
      case FileRemoved(data) => removeById((createId(data), data))
      case FileChanged(from, data) =>
        removeById((createId(from), from))
        cast(data).foreach(add)
    }
  }
}
