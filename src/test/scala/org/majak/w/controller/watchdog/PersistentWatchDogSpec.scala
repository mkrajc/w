package org.majak.w.controller.watchdog

import java.io.File
import java.util.Date

import org.junit.runner.RunWith
import org.majak.w.TestUtils
import org.majak.w.controller.watchdog.WatchDog.IndexResult
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class PersistentWatchDogSpec extends FlatSpec with Matchers {

  class PersistentWatchDogSpecClass extends PersistentWatchDog {
    override val indexName: String = "testPersistent"

    override def processIndex(currentIndex: IndexResult, previousIndex: IndexResult): Unit = {}

    override def rescan(index: IndexResult): IndexResult = ???

    override def file(): File = ???

    override def scan(): IndexResult = ???

    override val indexFile: File = new File(TestUtils.tempDir, "PersistentWatchDogSpecClass.dat")
  }

  "PersistentWatchDog" should "store and retrieve same index" in {
    val wd = new PersistentWatchDogSpecClass
    val index = Some(Index(fileData = Set(FileData("a", "b")), createdAt = new Date()))
    wd.store(index)
    val current = wd.index()
    assert(current === index)
  }
  
}
