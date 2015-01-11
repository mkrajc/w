package org.majak.w.controller.watchdog.image

import java.io.File

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.Thumbnails.Builder
import org.apache.commons.io.FileUtils
import org.apache.pivot.wtk.media.Picture
import org.majak.w.controller.ControllerSettings
import org.majak.w.controller.watchdog.FileData
import org.majak.w.controller.watchdog.sync.DirectoryWatchDogSynchronizer
import org.slf4j.LoggerFactory


class ThumbnailsSynchronizer(imgWatchDog: ImageDirectoryWatchDog) extends DirectoryWatchDogSynchronizer[ImageData](imgWatchDog) with ControllerSettings {
  val dir = new File(thumbnailsDir)
  val logger = LoggerFactory.getLogger(getClass)

  override def add(data: ImageData): Unit = ()

  override def remove(fileData: FileData): Unit = {
    FileUtils.forceDelete(new File(dir, fileData.name))
  }

  override def isOk(fileData: FileData): Boolean = {
    // TODO what if imagename is same but different content
    val b = new File(dir, fileData.name).exists()
    println(fileData.name + " " + b)
    b
  }

  override protected def create(fileData: FileData): ImageData = {
    logger.info("Create thumbnail for " + fileData.name)
    val t: Builder[File] = Thumbnails.of(new File(fileData.path))
      .size(200, 200)
      .outputFormat(fileData.ext)

    val file = t.toFile(new File(dir, fileData.name))
    val img = t.asBufferedImage()

    ImageData(fileData, new Picture(img))
  }
}
