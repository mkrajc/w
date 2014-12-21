
package org.majak.w.controller

import java.io.File

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.LoggerFactory

@RunWith(classOf[JUnitRunner])
class ImageDirectoryWatchDogSpec extends FlatSpec with Matchers {
  val logger = LoggerFactory.getLogger(getClass)

  "ImageDirectoryWatchDog" should "initialize if directory exist" in {
    val wd = new ImageDirectoryWatchDog(new File("""c:\Users\Martin\Pictures\"""))

    wd.observable.subscribe(println(_))

    wd.scan()
  }
}
