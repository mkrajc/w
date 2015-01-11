package org.majak.w.controller.watchdog

import java.io.{File, FileInputStream}
import java.util.Date

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils
import org.majak.w.controller.watchdog.WatchDog.IndexResult
import org.majak.w.rx.{Done, Event, ObservableObject}
import org.slf4j.LoggerFactory
import rx.lang.scala.Observable

sealed class WatchDogEvent extends Event

case class FileAdded(fileData: FileData) extends WatchDogEvent

case class FileRemoved(fileData: FileData) extends WatchDogEvent

case class FileChanged(before: FileData, after: FileData) extends WatchDogEvent

class DirectoryWatchDog(val directory: File) extends PersistentWatchDog with ObservableObject {
  val logger = LoggerFactory.getLogger(getClass)

  override val indexName = "data"
  // TODO
  override val indexFile: File = new File(".", "DirectoryWatchDog.dat")

  protected lazy val addSubject = createUiEventSubject[FileAdded]
  protected lazy val removedSubject = createUiEventSubject[FileRemoved]
  protected lazy val changedSubject = createUiEventSubject[FileChanged]
  protected lazy val doneNotifier = createUiEventSubject[Done.type]

  override def observable: Observable[WatchDogEvent] = addSubject.merge(removedSubject).merge(changedSubject)

  require(directory.exists(), "Not existing directory: " + directory)

  private def scanIntern(): IndexResult = {
    val fData: List[FileData] = scanFiles(directory.listFiles.toList, Nil)
    val (validFData, hexCollisions) = fData.groupBy(_.md5hex).partition(_._2.length == 1)

    if (hexCollisions.isEmpty) {
      val fileSet = validFData.flatMap(_._2).toSet
      Some(new Index(fileSet, new Date))
    } else {
      None
    }
  }

  override def rescan(index: IndexResult): IndexResult = {
    val newIdx = scanIntern()
    processIndex(newIdx, index)
    newIdx
  }

  override def scan(): IndexResult = {
    val current = scanIntern()
    notifyAsAdded(current)
    current
  }

  def scanFiles(files: List[File], fd: List[FileData]): List[FileData] = {
    if (files.isEmpty) {
      fd
    } else {
      val f = files.head

      if (f.isDirectory) {
        fd ::: scanFiles(f.listFiles.toList, Nil)
      } else {
        logger.trace("scanning file [" + f.getAbsolutePath + "]")
        var fis: FileInputStream = null
        try {
          fis = new FileInputStream(f)
          val hex = DigestUtils.md5Hex(fis)
          scanFiles(files.tail, FileData(f.getAbsolutePath, hex) :: fd)
        } finally {
          IOUtils.closeQuietly(fis)
        }
      }
    }
  }

  override def file(): File = directory

  private def processIndex(currentIndex: IndexResult, previousIndex: IndexResult): Unit = {
    def compareIndices(currentIndex: Index, previousIndex: Index): Unit = {
      val deletedOrChanged = previousIndex.fileData &~ currentIndex.fileData
      val addedOrChanged = currentIndex.fileData &~ previousIndex.fileData

      val (changedA, added) = addedOrChanged.partition(
        fd => deletedOrChanged.exists(e => e.path == fd.path || e.md5hex == fd.md5hex))
      val (changedD, deleted) = deletedOrChanged.partition(
        fd => addedOrChanged.exists(e => e.path == fd.path || e.md5hex == fd.md5hex))

      val changedStates = for {
        a <- changedA
        d <- changedD
        if a.path == d.path || a.md5hex == d.md5hex
      } yield (a, d)

      added.foreach(f => addSubject.onNext(FileAdded(f)))
      deleted.foreach(f => removedSubject.onNext(FileRemoved(f)))
      changedStates.foreach(p => changedSubject.onNext(FileChanged(p._1, p._2)))
      doneNotifier.onNext(Done)

    }

    if (previousIndex.isDefined) compareIndices(currentIndex.get, previousIndex.get)
    else notifyAsAdded(currentIndex)

  }

  private def notifyAsAdded(index: IndexResult): Unit = {
    index.foreach(i => i.fileData.foreach(f => addSubject.onNext(FileAdded(f))))
    doneNotifier.onNext(Done)
  }

}


