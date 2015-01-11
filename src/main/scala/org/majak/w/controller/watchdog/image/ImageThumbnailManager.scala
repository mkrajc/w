package org.majak.w.controller.watchdog.image

import java.awt.image.BufferedImage
import java.io.File

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.Thumbnails.Builder
import org.apache.commons.io.FileUtils
import org.apache.pivot.wtk.media.Picture
import org.majak.w.controller.ControllerSettings
import org.majak.w.controller.watchdog.FileData


/**
 * Creates and manages images thumbnail
 */
class ImageThumbnailManager extends ControllerSettings {

  val dir = new File(thumbnailsDir)

  FileUtils.forceMkdir(dir)

  def createThumbnail(imgData: ImageData): ImageData = {
    println("create thumb " + imgData)
    imgData.img match {
      case p: Picture => createThumbImage(imgData, p.getBufferedImage)
      case _ => ???
    }
    imgData
  }

  def createThumbnail(fileData: FileData): Unit = {
    val t: Builder[File] = Thumbnails.of(new File(fileData.path))
      .size(200, 200)
      .outputFormat(fileData.ext)

    t.toFile(new File(dir, fileData.name))
    t.asBufferedImage()
  }

  private def createThumbImage(imgData: ImageData, i: BufferedImage): Unit = {
    Thumbnails.of(i)
      .size(200, 200)
      .outputFormat(imgData.fileData.ext)
      .toFile(new File(dir, imgData.fileData.name))
  }


}
