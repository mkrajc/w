package org.majak.w.controller

import java.io.{File, FileInputStream}
import java.util.Date

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils
import org.majak.w.rx.{ObservableObject, Event}
import org.slf4j.LoggerFactory
import rx.lang.scala.Observable

sealed class WatchDogEvent extends  Event
case class FileAdded(fileData: FileData) extends WatchDogEvent

case class FileRemoved(fileData: FileData) extends WatchDogEvent

case class FileChanged(before: FileData, after: FileData) extends WatchDogEvent

class DirectoryWatchDog(val directory: File) extends ObservableObject {
  val logger = LoggerFactory.getLogger(getClass)

  private lazy val subject = createUiEventSubject[WatchDogEvent]
  override def observable: Observable[WatchDogEvent] = subject

  type IndexResult = Either[Index, List[String]]

  require(directory.exists(), "Not existing directory: " + directory)

  private def scanIntern(): IndexResult = {
    val fData = scanFiles(directory.listFiles.toList, Nil)
    val (validFData, hexCollisions) = fData.groupBy(_.md5hex).partition(_._2.length == 1)

    if (hexCollisions.isEmpty) {
      val fileSet = validFData.flatMap(_._2).toSet
      Left(new Index(fileSet, new Date))
    } else {
      Right(hexCollisions.map(f => "Collisions [" + f._1 + "] on files: " + f._2.mkString).toList)
    }
  }

  def scan(): IndexResult = {
    val ir = scanIntern()
    ir match {
      case Right(errors) => Right("Cannot scan directory." :: errors)
      case Left(index) => {
        index.fileData.foreach(f => subject.onNext(FileAdded(f)))
      }
    }
    ir
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

  def rescan(lastIndex: Index): IndexResult = {

    def compareIndices(lastIndex: Index, currentIndex: Index): IndexResult = {
      val addedOrChanged = currentIndex.fileData &~ lastIndex.fileData
      val deletedOrChanged = lastIndex.fileData &~ currentIndex.fileData

      val (changedA, added) = addedOrChanged.partition(
        fd => deletedOrChanged.exists(e => e.path == fd.path || e.md5hex == fd.md5hex))
      val (changedD, deleted) = deletedOrChanged.partition(
        fd => addedOrChanged.exists(e => e.path == fd.path || e.md5hex == fd.md5hex))

      val changedStates = for {
        a <- changedA
        d <- changedD
        if a.path == d.path || a.md5hex == d.md5hex
      } yield (a, d)

      added.foreach(f => subject.onNext(FileAdded(f)))
      deleted.foreach(f => subject.onNext(FileRemoved(f)))
      changedStates.foreach(p => subject.onNext(FileChanged(p._1, p._2)))

      Left(currentIndex)
    }

    val ir = scanIntern()
    ir match {
      case Right(errors) => Right("Cannot scan directory." :: errors)
      case Left(index) => compareIndices(lastIndex, index)
    }

  }

}

case class FileData(path: String, md5hex: String)

case class Index(fileData: Set[FileData], createdAt: Date)