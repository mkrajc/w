package org.majak.w.controller.watchdog.image

import java.io.File

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.Thumbnails.Builder
import net.coobird.thumbnailator.tasks.UnsupportedFormatException
import org.apache.commons.io.FileUtils
import org.apache.pivot.wtk.media.{Image, Picture}
import org.majak.w.controller.ControllerSettings
import org.majak.w.controller.watchdog.FileData
import org.majak.w.controller.watchdog.sync.DirectoryWatchDogSynchronizer
import org.majak.w.utils.Utils
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer


case class Thumbnail(source: FileData, thumbImage: Image) {

  def loadImage(): Image = {
    Image.loadFromCache(new File(source.path).toURI.toURL)
  }

  override def equals(o: scala.Any): Boolean = {
    o match {
      case Thumbnail(src, _) => src == source
      case _ => false
    }
  }
}

class ThumbnailsSynchronizer(imgWatchDog: ImageDirectoryWatchDog) extends DirectoryWatchDogSynchronizer[Thumbnail](imgWatchDog) with ControllerSettings {
  val dir = new File(thumbnailsDir)
  val logger = LoggerFactory.getLogger(getClass)

  if(!dir.exists()){
    FileUtils.forceMkdir(dir)
  }

  private val thumbnailBuilder = new ListBuffer[Thumbnail]()

  override def add(data: Thumbnail): Unit = {
    if (data.thumbImage == null) {
      imgWatchDog.deleteFile(data.source)
    } else {
      addThumbnail(data)
    }
  }

  override def remove(fileData: FileData): Unit = {
    logger.info("Deleting thumbnail for " + fileData.name)
    FileUtils.forceDelete(createThumbFile(fileData))
    thumbnailBuilder.find(_.source == fileData).map(thumbnailBuilder.-=)
  }

  override def isOk(fileData: FileData): Boolean = {
    // TODO what if image name is same but different content
    val thumbFile = createThumbFile(fileData)
    val ok = thumbFile.exists()
    logger.info("Checking thumbnail for " + fileData.name + "\t[" + Utils.okOrFailed(ok) + "]")

    if (ok) {
      addThumbnail(Thumbnail(fileData, Image.loadFromCache(thumbFile.toURI.toURL)))
    }

    ok
  }

  override protected def create(fileData: FileData): Thumbnail = {
    logger.info("Create thumbnail for " + fileData.name)
    val t: Builder[File] = Thumbnails.of(new File(fileData.path))
      .size(THUMBS_SIZE, THUMBS_SIZE)
      .outputFormat(fileData.ext)

    val bufImg = try {
      t.toFile(createThumbFile(fileData))
      new Picture(t.asBufferedImage())
    } catch {
      case ioe: UnsupportedFormatException =>
        logger.error("Could not create thumbnail for " + fileData.path)
        null
    }

    Thumbnail(fileData, bufImg)
  }

  def thumbs: Set[Thumbnail] = thumbnailBuilder.toSet

  private def addThumbnail(thumbnail: Thumbnail): Unit = {
    if (!thumbnailBuilder.contains(thumbnail)) {
      thumbnailBuilder += thumbnail
    }
  }

  private def createThumbFile(origin: FileData): File = new File(dir, origin.name)

}
