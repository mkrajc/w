package org.majak.w.controller.watchdog

import java.io.File
import java.util.Date

import org.apache.commons.io.FileUtils
import org.junit.runner.RunWith
import org.majak.w.TestableDir
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class PersistentWatchDogSpec extends FlatSpec with Matchers with TestableDir {

  class PersistentWatchDogSpecClass(override val indexFile: File, override val file: File)
    extends DirectoryWatchDog(file) {

    override val indexName: String = "testPersistentWatchDog"

  }

  "PersistentWatchDog" should "store and retrieve same index" in {
    testInTestDir(tmpRandDir) { f =>
      val watchedDir = tmpRandDir
      val wd = new PersistentWatchDogSpecClass(new File(f, "index"), watchedDir )
      val index = Some(Index(fileData = Set(FileData("a", "b")), createdAt = new Date()))
      wd.store(index)
      val current = wd.index()
      assert(current === index)

      FileUtils.deleteQuietly(watchedDir)
    }
  }


  it should "store index on refresh" in {
    testInTestDir(tmpRandDir) { f =>
      val watchedDir = tmpRandDir
      val wd = new PersistentWatchDogSpecClass(new File(f, "index"), watchedDir)

      prepareFilesInDir(watchedDir, Map("one" -> "1", "two" -> "2"))

      wd.refresh()

      val index = wd.index()

      index.get shouldNot be(null)
      index.get.fileData.size should equal(2)

      prepareFilesInDir(watchedDir, Map("three" -> "3", "four" -> "4"))

      wd.refresh()

      val indexAfterChange = wd.index()
      indexAfterChange.get.fileData.size should equal(4)

      FileUtils.deleteQuietly(watchedDir)
    }
  }


}
