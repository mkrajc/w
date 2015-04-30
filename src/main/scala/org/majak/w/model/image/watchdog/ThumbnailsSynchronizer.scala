package org.majak.w.model.image.watchdog

import java.io.File

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.Thumbnails.Builder
import net.coobird.thumbnailator.tasks.UnsupportedFormatException
import org.apache.commons.io.FileUtils
import org.apache.pivot.wtk.media.{Image, Picture}
import org.majak.w.controller.watchdog.FileData
import org.majak.w.controller.watchdog.sync.DirectoryWatchDogSynchronizer
import org.majak.w.di.AppSettings
import org.majak.w.model.image.data.Thumbnail
import org.majak.w.utils.Utils

import scala.collection.mutable.ListBuffer

class ThumbnailsSynchronizer(imgWatchDog: ImageDirectoryWatchDog) extends DirectoryWatchDogSynchronizer[Thumbnail, File](imgWatchDog) with AppSettings {
  val dir = new File(thumbnailsDir)

  if (!dir.exists()) {
    FileUtils.forceMkdir(dir)
  }

  private val thumbnailBuilder = new ListBuffer[Thumbnail]()

  override def add(data: Thumbnail): Unit = {
    if (data.thumbImage == null) {
      imgWatchDog.ignoreFile(data.source)
    } else {
      addThumbnail(data)
    }
  }

  override protected def cast(fileData: FileData): Option[Thumbnail] = {
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

    Some(Thumbnail(fileData, bufImg))
  }

  def thumbs: Set[Thumbnail] = thumbnailBuilder.toSet

  private def addThumbnail(thumbnail: Thumbnail): Unit = {
    if (!thumbnailBuilder.contains(thumbnail)) {
      thumbnailBuilder += thumbnail
    }
  }

  private def createThumbFile(origin: FileData): File = {
    val thumbFile = new File(dir, origin.path.diff(imageDir.getAbsolutePath))
    FileUtils.forceMkdir(new File(thumbFile.getParent))
    thumbFile
  }

  override protected def createId(fileData: FileData): File = createThumbFile(fileData)

  override def removeById(id: (File, FileData)): Boolean = {
    logger.info("Deleting thumbnail file for " + id._2.name)
    FileUtils.forceDelete(id._1)
    thumbnailBuilder.find(_.source == id._2).map(thumbnailBuilder.-=)
    true
  }

  override def objectWithIdExists(id: (File, FileData)): Boolean = {
    val ok = id._1.exists()
    logger.info("Checking thumbnail for " + id._1.getName + "\t[" + Utils.okOrFailed(ok) + "]")

    if (ok) {
      addThumbnail(Thumbnail(id._2, Image.load(createThumbFile(id._2).toURI.toURL)))
    }

    ok
  }
}
