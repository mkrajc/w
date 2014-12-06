package org.majak.w.controller

import java.io.{File, FileInputStream}
import java.util.Date

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils


class DirectoryWatchDog(val directory: File) {

  type IndexResult = Either[Index, List[String]]
  type AddFileDataHandler = FileData => Unit
  type RemoveFileDataHandler = FileData => Unit
  type ChangeFileDataHandler = (FileData, FileData) => Unit

  require(directory.exists(), "Not existing directory: " + directory)

  var addHandlers: List[AddFileDataHandler] = Nil
  var removedHandlers: List[RemoveFileDataHandler] = Nil
  var changedHandlers: List[ChangeFileDataHandler] = Nil

  def scan(): IndexResult = {
    val fData = scanFiles(directory.listFiles.toList, Nil)
    val (validFData, hexCollisions) = fData.groupBy(_.md5hex).partition(_._2.length == 1)

    if (hexCollisions.isEmpty) Left(new Index(validFData.flatMap(_._2).toSet, new Date))
    else Right(hexCollisions.map(f => "Collisions [" + f._1 + "] on files: " + f._2.mkString).toList)
  }

  def scanFiles(files: List[File], fd: List[FileData]): List[FileData] = {
    if (files.isEmpty) {
      fd
    } else {
      val f = files.head

      if (f.isDirectory) {
        fd ::: scanFiles(f.listFiles.toList, Nil)
      } else {
        println("TRACE: scanning file [" + f.getAbsolutePath + "]")
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
      } yield (a,d)

      added.foreach(f => addHandlers.foreach(_(f)))
      deleted.foreach(f => removedHandlers.foreach(_(f)))
      changedStates.foreach(p => changedHandlers.foreach(_(p._1,p._2)))

      Left(currentIndex)
    }

    val ir = scan()
    ir match {
      case Right(errors) => Right("Cannot scan directory." :: errors)
      case Left(index) => compareIndices(lastIndex, index)
    }

  }

  def addAddFileDataHandler(h: AddFileDataHandler) = addHandlers = h :: addHandlers
  def addRemovedFileDataHandler(h: RemoveFileDataHandler) = removedHandlers = h :: removedHandlers
  def addChangedFileDataHandler(h: ChangeFileDataHandler) = changedHandlers = h :: changedHandlers

}

case class FileData(path: String, md5hex: String)

case class Index(fileData: Set[FileData], createdAt: Date)