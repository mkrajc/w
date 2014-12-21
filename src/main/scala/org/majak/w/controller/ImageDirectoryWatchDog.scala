
package org.majak.w.controller

import java.io.File

import org.apache.pivot.wtk.media.Image
import rx.lang.scala.Subject

class ImageDirectoryWatchDog(file: File) {
  val observable = Subject[Image]()

  private val dirWd = new DirectoryWatchDog(file)
  dirWd.addAddFileDataHandler(checkFileData(_).map(i => observable.onNext(i)))

  println("file: " + file)

  def checkFileData(fileData: FileData): Option[Image] = {
    try {
      Some(Image.load(new File(fileData.path).toURI.toURL))
    } catch {
      case e: Throwable => println(s"filedata ${fileData.path} " + e.getMessage); None
    }

  }

  def scan(): Either[Index, List[String]] = {
    dirWd.scan()
  }

}
