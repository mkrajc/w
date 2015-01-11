
package org.majak.w.controller.watchdog.image

import java.io.File

import org.apache.pivot.util.concurrent.Task
import org.apache.pivot.wtk.media.Image
import org.majak.w.controller.watchdog.{DirectoryWatchDog, FileAdded, FileData}
import rx.lang.scala.Subject

case class ImageData(fileData: FileData, img: Image)

class ImageDirectoryWatchDog(file: File) extends DirectoryWatchDog(file) {
  val imagesLoaded = Subject[Seq[ImageData]]()
  val imageLoaded = Subject[Image]()

  override val indexName: String = "images"
  override val indexFile: File = new File(indexDir, indexName)
  //val thumbnailer = new ImageThumbnailManager


  val addImageSubject = addSubject.filter(hasImageExtension)


  addSubject.tumblingBuffer(doneNotifier).subscribe(onNext = seq => {

    /*if (seq.nonEmpty) {
      val task = new LoadImagesTask(seq.map(f => f.fileData))

      val tl: TaskListener[Seq[ImageData]] = new TaskAdapter[Seq[ImageData]](new TaskListener[Seq[ImageData]] {
        override def executeFailed(task: Task[Seq[ImageData]]): Unit = {}

        override def taskExecuted(task: Task[Seq[ImageData]]): Unit = {

          imagesLoaded.onNext(task.getResult)
          task.getResult.map(id => thumbnailer.createThumbnail(id))
        }
      })

      task.execute(tl)
    }*/

    //seq.map(e => thumbnailer.createThumbnail(e.fileData))
  })

  def hasImageExtension(fileAdded: FileAdded): Boolean = {
    val path = fileAdded.fileData.path
    val lastDotIndex = path.lastIndexOf(".")
    val ext = path.substring(lastDotIndex + 1).toLowerCase
    List("jpg", "jpeg", "png", "gif", "bmp").contains(ext)
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
