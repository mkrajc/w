package org.majak.w.controller

import java.io.File


trait ControllerSettings {
  val errorsDirName = ".errors"

  val dataDirPath = """.\data"""
  private val imageDirPath = dataDirPath + """\images"""

  val imageDir = new File(imageDirPath)


  val appDir = """.\.app"""
  val indexDir = appDir + """\index"""
  val thumbnailsDir = appDir + """\thumbs"""

  val THUMBS_SIZE = 200


}
