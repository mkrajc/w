
package org.majak.w.controller

import java.io.File

import org.apache.pivot.util.concurrent.{TaskListener, Task}
import org.apache.pivot.wtk.TaskAdapter
import org.apache.pivot.wtk.media.Image
import rx.lang.scala.Subject

class ImageDirectoryWatchDog(file: File) {
  val observable = Subject[Image]()

  private val dirWd = new DirectoryWatchDog(file)
  //dirWd.addAddFileDataHandler(checkFileData)

  def checkFileData(fileData: FileData): Unit = {

    val task = new LoadImageTask(fileData)

    task.execute(new TaskAdapter[Image](new TaskListener[Image] {
      override def executeFailed(task: Task[Image]): Unit = { }

      override def taskExecuted(task: Task[Image]): Unit = {
        println("done loading:" + fileData.path)
        observable.onNext(task.getResult)
      }
    }))

  }

  def scan(): Either[Index, List[String]] = {
    dirWd.scan()
  }

}


class LoadImageTask(fileData: FileData) extends Task[Image] {

  override def execute(): Image = {
    println("start loading:" + fileData.path)
    Image.load(new File(fileData.path).toURI.toURL)
  }
}
