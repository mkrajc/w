
package org.majak.w.controller.watchdog

import java.io.File
import java.net.URL

import org.apache.pivot.util.concurrent.{Task, TaskListener}
import org.apache.pivot.wtk.TaskAdapter
import org.apache.pivot.wtk.media.Image
import org.majak.w.rx.Event
import rx.lang.scala.Subject

case class ImageAdded(fileData: FileData, fullImage: Image, thumbnail: Image) extends Event

class ImageDirectoryWatchDog(file: File) extends DirectoryWatchDog(file) {
  val imagesLoaded = Subject[Seq[Image]]()
  val imageLoaded = Subject[Image]()

  override val indexName: String = "images_dir"

  val addImageSubject = addSubject.filter(hasImageExtension)


  addSubject.tumblingBuffer(doneNotifier).subscribe(onNext = seq => {
    val start = System.currentTimeMillis()
    println("loading images ...")


    //val task = new LoadImagesTask(seq.map(f => f.fileData))

    val tl: TaskListener[Seq[Image]] = new TaskAdapter[Seq[Image]](new TaskListener[Seq[Image]] {
      override def executeFailed(task: Task[Seq[Image]]): Unit = {}

      override def taskExecuted(task: Task[Seq[Image]]): Unit = {
        println("done loading images " + (System.currentTimeMillis() - start) + "ms")
        imagesLoaded.onNext(task.getResult)
      }
    })

    val tl2: TaskListener[Image] = new TaskAdapter[Image](new TaskListener[Image] {
      override def executeFailed(task: Task[Image]): Unit = {}

      override def taskExecuted(task: Task[Image]): Unit = {
        println("done loading image " + (System.currentTimeMillis() - start) + "ms: " + task.getResult)
        imageLoaded.onNext(task.getResult)
      }
    })

    seq.map(f => {
      println("start loading: " + f.fileData.path)
      val url: URL = new File(f.fileData.path).toURI.toURL
      Image.load(url, tl2)
    })

    //task.execute(tl)
  })

  def hasImageExtension(fileAdded: FileAdded): Boolean = {
    val path = fileAdded.fileData.path
    val lastDotIndex = path.lastIndexOf(".")
    val ext = path.substring(lastDotIndex + 1).toLowerCase
    List("jpg","jpeg","png","gif","bmp").contains(ext)
  }
}


class LoadImagesTask(data: Seq[FileData]) extends Task[Seq[Image]] {

  override def execute(): Seq[Image] = {
    val images: Seq[Option[Image]] = data.map(f =>
      try {
        val start = System.currentTimeMillis()
        println("start loading: " + f.path)
        val image = Some(Image.load(new File(f.path).toURI.toURL))
        println("done loading: " + f.path + " (" + (System.currentTimeMillis() - start) + "ms)")
        image
      } catch {
        case e: Exception =>
          println("failed loading for: " + f.path + " " + e.getMessage)
          None
      }
    )
    images.flatten
  }
}

class LoadImageTask(f: FileData) extends Task[Image] {
  override def execute(): Image = {

    try {
      val start = System.currentTimeMillis()
      println("start loading: " + f.path)
      val image = Image.load(new File(f.path).toURI.toURL)
      println("done loading: " + f.path + " (" + (System.currentTimeMillis() - start) + "ms)")
      image
    } catch {
      case e: Exception =>
        println("failed loading for: " + f.path + " " + e.getMessage)
        e.printStackTrace()
        throw e
    }
  }
}
