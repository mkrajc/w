package org.majak.w.di

import java.io.File


trait AppSettings {
  val ignoredDirName = ".ignored"

  val dataDirPath = """.\data"""
  private val imageDirPath = dataDirPath + """\images"""
  private val songDirPath = dataDirPath + """\songs"""

  val imageDir = new File(imageDirPath)
  val songsDir = new File(songDirPath)


  val appDir = """.\.app"""
  val indexDir = appDir + """\index"""
  val thumbnailsDir = appDir + """\thumbs"""

  val THUMBS_SIZE = 200


}
