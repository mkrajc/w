package org.majak.w.controller.watchdog

import java.io.{FileInputStream, File}
import java.util.Date

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.{IOUtils, FileUtils}
import org.apache.commons.io.filefilter.{TrueFileFilter, NameFileFilter, NotFileFilter}
import org.majak.w.controller.watchdog.WatchDog.IndexResult
import org.majak.w.di.AppSettings
import org.majak.w.rx.{Done, Event, ObservableObject}
import org.majak.w.utils.Utils
import org.slf4j.LoggerFactory
import rx.lang.scala.Observable

import scala.collection.JavaConversions._

sealed abstract class WatchDogEvent extends Event

case class FileAdded(fileData: FileData) extends WatchDogEvent

case class FileRemoved(fileData: FileData) extends WatchDogEvent

case class FileChanged(before: FileData, after: FileData) extends WatchDogEvent

class DirectoryWatchDog(val directory: File) extends PersistentWatchDog with ObservableObject with AppSettings {
  val logger = LoggerFactory.getLogger(getClass)

  override val indexName = "data"
  override val indexFile: File = new File(indexDir, "DirectoryWatchDog.dat")

  val supportedExtensions: Set[String] = Nil.toSet

  private val dirFilter = new NotFileFilter(new NameFileFilter(ignoredDirName))

  protected lazy val addSubject = createEventSubject[FileAdded]
  protected lazy val removedSubject = createEventSubject[FileRemoved]
  protected lazy val changedSubject = createEventSubject[FileChanged]
  protected lazy val doneNotifier = createEventSubject[Done.type]

  override def observable: Observable[WatchDogEvent] = addSubject.merge(removedSubject).merge(changedSubject).filter(isFileSupported)

  if(!directory.exists()){
    logger.info("Creating directory: " + directory)
    FileUtils.forceMkdir(directory)
  }

  require(directory.exists(), "Not existing directory: " + directory)

  private def scanIntern(): IndexResult = {
    val fData: List[FileData] = scanFiles(directory)
    val (validFData, hexCollisions) = fData.groupBy(_.md5hex).partition(_._2.length == 1)

    if (hexCollisions.isEmpty) {
      val fileSet = validFData.flatMap(_._2).toSet
      Some(new Index(fileSet, new Date))
    } else {
      None
    }
  }

  def filterFiles(fileData: List[File]): List[File] = {
    if (supportedExtensions.isEmpty) {
      fileData
    } else {
      val (filteredList, ignored) = fileData.partition(fd => supportFile(fd.getName))

      if (ignored.nonEmpty) {
        ignored.foreach(ignoreFile)
      }

      filteredList
    }
  }

  def isFileSupported(event: WatchDogEvent): Boolean = {
    val path = event match {
      case a: FileAdded => a.fileData.path
      case r: FileRemoved => r.fileData.path
      case ch: FileChanged => ch.after.path
    }

    supportFile(path)
  }

  def supportFile(filename: String): Boolean = {
    if (supportedExtensions.isEmpty) true
    else {
      supportedExtensions.contains(Utils.extension(filename))
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

  def scanFiles(root: File): List[FileData] = {
    val files = FileUtils.listFiles(root, TrueFileFilter.INSTANCE, dirFilter).toList

    filterFiles(files).map(f => {
      logger.trace("Scanning file [" + f.getAbsolutePath + "]")
      var fis: FileInputStream = null
      try {
        fis = new FileInputStream(f)
        val hex = DigestUtils.md5Hex(fis)
        val ext = Utils.extension(f.getName)
        FileData(f.getAbsolutePath, hex, f.getName, ext)
      } finally {
        IOUtils.closeQuietly(fis)
      }
    })
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
      changedStates.foreach(p => changedSubject.onNext(FileChanged(p._2, p._1)))
      doneNotifier.onNext(Done)

    }

    if (previousIndex.isDefined) compareIndices(currentIndex.get, previousIndex.get)
    else notifyAsAdded(currentIndex)

  }

  def ignoreFile(file: File): Unit = {
    logger.info("Ignoring file : " + file.getPath)
    FileUtils.moveFileToDirectory(file, new File(directory, ignoredDirName), true)
  }

  def ignoreFile(fileData: FileData): Unit = {
    ignoreFile(new File(fileData.path))
  }

  private def notifyAsAdded(index: IndexResult): Unit = {
    index.foreach(i => i.fileData.foreach(f => addSubject.onNext(FileAdded(f))))
    doneNotifier.onNext(Done)
  }

}