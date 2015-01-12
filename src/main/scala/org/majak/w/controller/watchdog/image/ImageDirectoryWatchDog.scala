
package org.majak.w.controller.watchdog.image

import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.pivot.util.concurrent.Task
import org.apache.pivot.wtk.media.Image
import org.majak.w.controller.watchdog._
import rx.lang.scala.Observable

case class ImageData(fileData: FileData, img: Image)

class ImageDirectoryWatchDog(file: File) extends DirectoryWatchDog(file) {

  override def observable: Observable[WatchDogEvent] = {
    super.observable.filter(isImageRelated)
  }

  override val indexName: String = "images"
  override val indexFile: File = new File(indexDir, indexName)

  override def list(): Set[FileData] = {
    val (filteredList, a) = super.list().partition(fd => hasImageExtension(fd.name))

    if (a.nonEmpty) {
      a.foreach(deleteFile)
    }

    filteredList

  }

  def deleteFile(fileData: FileData): Unit = {
    logger.info("Deleting file because not an image: " + fileData.path)
    FileUtils.deleteQuietly(new File(fileData.path))
  }

  def hasImageExtension(path: String): Boolean = {
    val lastDotIndex = path.lastIndexOf(".")
    val ext = path.substring(lastDotIndex + 1).toLowerCase
    List("jpg", "jpeg", "png", "gif", "bmp").contains(ext)
  }

  def isImageRelated(event: WatchDogEvent): Boolean = {
    val path = event match {
      case a: FileAdded => a.fileData.path
      case r: FileRemoved => r.fileData.path
      case ch: FileChanged => ch.after.path
    }

    hasImageExtension(path)
  }

}

class LoadImagesTask(data: Seq[FileData]) extends Task[Seq[ImageData]] {

  override def execute(): Seq[ImageData] = {
    val start = System.currentTimeMillis()
    val images: Seq[Option[ImageData]] = data.map(f =>
      try {
        println("start loading: " + f.path)
        val image = Some(ImageData(f, Image.load(new File(f.path).toURI.toURL)))
        println("done loading: " + f.path + " (" + (System.currentTimeMillis() - start) + "ms)")
        image
      } catch {
        case e: Exception =>
          println("failed loading for: " + f.path + " " + e.getMessage)
          None
      }
    )
    println("done loading images " + (System.currentTimeMillis() - start) + "ms")
    images.flatten
  }
}
