package org.majak.w.controller

import java.io.{File, FileInputStream}

import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.IOUtils

class DirectoryWatchDog(val directory: File) {

  require(directory.exists(), "Not existing directory: " + directory)

  def scan(): Either[Index, List[String]] = {
    val fData = scanFiles(directory.listFiles.toList, Nil)
    val (validFData, hexCollisions) = fData.groupBy(_.md5hex).partition(_._2.length == 1)

    if (hexCollisions.isEmpty) Left(new Index(validFData.flatMap(_._2).toList))
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
}

case class FileData(path: String, md5hex: String)

case class Index(fileData: List[FileData])