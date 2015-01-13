
package org.majak.w.model.image.watchdog

import java.io.File

import org.apache.pivot.wtk.media.Image
import org.majak.w.controller.watchdog._

import scala.collection.immutable.HashSet

case class ImageData(fileData: FileData, img: Image)

class ImageDirectoryWatchDog(file: File) extends DirectoryWatchDog(file) {

  override val indexName: String = "images"
  override val indexFile: File = new File(indexDir, indexName)

  override val supportedExtensions: Set[String] = HashSet("jpg", "jpeg", "png", "gif", "bmp")

}