package org.majak.w.controller

import java.io.{File, FileInputStream}

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils
import org.majak.w.controller.DirectoryWatchDog.IndexResult

object DirectoryWatchDog {
  type IndexResult = Either[Index, List[String]]
}

class DirectoryWatchDog(val directory: File) {



  require(directory.exists(), "Not existing directory: " + directory)

  var handlers: List[WatchDogHandler] = Nil

  def scan(): IndexResult = {
    val fData = scanFiles(directory.listFiles.toList, Nil)
    val (validFData, hexCollisions) = fData.groupBy(_.md5hex).partition(_._2.length == 1)

    if (hexCollisions.isEmpty) Left(new Index(validFData.flatMap(_._2).toSet))
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
      val added = currentIndex.fileData &~ lastIndex.fileData
      println("added:" + added.mkString)
      val deleted = lastIndex.fileData &~ currentIndex.fileData
      println("deleted:" + deleted.mkString)

      if(!added.isEmpty){
        handlers.foreach(_.onFilesAdded(added))
      }

      if(!deleted.isEmpty){
        handlers.foreach(_.onFilesRemoved(added))
      }

      Left(currentIndex)
    }

    val ir = scan()
    ir match {
      case Right(errors) => Right("Cannot scan directory." :: errors)
      case Left(index) => compareIndices(lastIndex, index)
    }

  }

  def addHandler(h: WatchDogHandler) = handlers = h :: handlers
}

trait WatchDogHandler {
  def onFilesRemoved(removed: Set[FileData])

  def onFilesChanged(changed: Set[FileData])

  def onFilesAdded(added: Set[FileData])
}

case class FileData(path: String, md5hex: String)

case class Index(fileData: Set[FileData])