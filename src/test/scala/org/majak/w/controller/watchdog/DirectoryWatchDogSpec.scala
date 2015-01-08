package org.majak.w.controller.watchdog

import java.io.File
import java.util.UUID

import org.apache.commons.io.FileUtils
import org.junit.runner.RunWith
import org.majak.w.TestUtils
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.LoggerFactory

@RunWith(classOf[JUnitRunner])
class DirectoryWatchDogSpec extends FlatSpec with Matchers with MockitoSugar {
  val logger = LoggerFactory.getLogger(getClass)

  "DirectoryWatchDog" should "initialize if directory exist" in {
    val wd = new DirectoryWatchDog(TestUtils.tempDir)
    wd shouldNot be(null)
  }

  it should "failed initialization if directory does not exist" in {
    val neDir = "\\a\\a\\a"
    val thrown = intercept[Exception] {
      new DirectoryWatchDog(new File(neDir))
    }
    assert(thrown.getMessage.contains("Not existing directory: " + neDir))
  }

  it should "provide data when scanning nonempty directory" in {
    testInTestDir { f =>
      prepareFilesInDir(f, Map("a" -> "a", "b" -> "b"))
      val wd = new DirectoryWatchDog(f)
      val data = wd.scan()
      val index = data.left.get
      index shouldBe a[Index]
      assert(index.fileData.size === 2)
    }
  }

  it should "provide data when scanning nonempty directory recursively" in {
    testInTestDir { f =>
      prepareFilesInDir(f, Map("a" -> "a", "b" -> "b"))

      val nested = new File(f, "nested")
      nested.mkdir()

      prepareFilesInDir(nested, Map("c" -> "c"))

      val wd = new DirectoryWatchDog(f)
      val data = wd.scan()
      val index = data.left.get
      index shouldBe a[Index]
      assert(index.fileData.size === 3)
    }
  }

  it should "provide errors when scanning duplicate content" in {

    testInTestDir { f =>
      prepareFilesInDir(f, Map("one" -> "same", "two" -> "same"))
      val wd = new DirectoryWatchDog(f)
      val data = wd.scan()
      val errors = data.right.get
      errors shouldBe a[List[String]]
      assert(errors.size === 1)
      assert(errors(0).contains("one"))
      assert(errors(0).contains("two"))
    }
  }

  it should "provide data when scanning with read-only files" in {

    testInTestDir { f =>
      prepareFilesInDir(f, Map("a" -> "a", "b" -> "b"))
      val wd = new DirectoryWatchDog(f)
      val data = wd.scan()
      val index = data.left.get
      index shouldBe a[Index]
      assert(index.fileData.size === 2)
    }
  }

  it should "notify on first scan everything as added" in {
    testInTestDir { f =>
      prepareFilesInDir(f, Map("a" -> "a", "b" -> "b"))
      val wd = new DirectoryWatchDog(f)

      var added = 0
      val h = (fd: FileData) => {
        added = added + 1
        logger.info("added " + fd.path)
      }
      wd.observable.subscribe(onNext = e => e match {
        case FileAdded(f) => h(f)
        case _ => ()
      })

      val data = wd.scan()

      assert(added === 2)
    }
  }

  it should "not notify on rescan when nothing changed" in {
    testInTestDir { f =>
      prepareFilesInDir(f, Map("a" -> "a", "b" -> "b"))
      val wd = new DirectoryWatchDog(f)
      val data = wd.scan()
      assert(data.left.get.fileData === wd.rescan(data.left.get).left.get.fileData)
    }
  }

  it should "be notified when new files are added to directory" in {

    testInTestDir { f =>


      prepareFilesInDir(f, Map("a" -> "a", "b" -> "b"))
      val wd = new DirectoryWatchDog(f)
      val data = wd.scan()

      var added = 0
      val h = (fd: FileData) => {
        added = added + 1
        logger.info("added " + fd.path)
      }
      wd.observable.subscribe(onNext = e => e match {
        case FileAdded(f) => h(f)
        case _ => ()
      })

      val index = data.left.get

      prepareFilesInDir(f, Map("c" -> "c", "d" -> "d"))
      wd.rescan(index)

      assert(added === 2)

    }
  }

  it should "be notified when some files are deleted from directory" in {

    testInTestDir { f =>
      val files = prepareFilesInDir(f, Map("a" -> "a", "b" -> "b"))
      val wd = new DirectoryWatchDog(f)

      var deleted = 0
      val h = (fd: FileData) => {
        deleted = deleted + 1
        logger.info("deleted " + fd.path)
      }
      wd.observable.subscribe(onNext = _ match {
        case FileRemoved(f) => h(f)
        case _ => ()
      })

      val data = wd.scan()
      val index = data.left.get

      files.foreach(FileUtils.deleteQuietly)

      wd.rescan(index)

      assert(deleted === 2)
    }
  }

  it should "be notified when some files are changed in directory" in {

    testInTestDir { f =>
      val fileChanged = prepareFilesInDir(f, Map("a" -> "a"))
      val fileRenamed = prepareFilesInDir(f, Map("b" -> "b"))
      val fileRenAndChanged = prepareFilesInDir(f, Map("c" -> "c"))

      val wd = new DirectoryWatchDog(f)

      var changed = 0
      val h = (fdCurrent: FileData, fdPrevious: FileData) => {
        changed = changed + 1
      }

      wd.observable.subscribe(onNext = _ match {
        case changed: FileChanged => h(changed.before, changed.after)
        case _ => ()
      })

      val data = wd.scan()
      val index = data.left.get

      fileChanged.foreach(FileUtils.write(_, "+append", true))
      fileRenamed.foreach(f => f.renameTo(new File(f.getParent, "renamed")))
      fileRenAndChanged.foreach(FileUtils.write(_, "+append", true))
      fileRenAndChanged.foreach(f => f.renameTo(new File(f.getParent, "new")))

      wd.rescan(index)

      assert(changed === 2)
    }
  }

  def prepareFilesInDir(dir: File, map: Map[String, String]) = {
    map.map(kv => createFile(dir, kv._1, kv._2))
  }

  val tmpDir: File = TestUtils.tempDir

  def testInTestDir(test: File => Unit): Unit = {
    val dir = new File(tmpDir, UUID.randomUUID().toString)
    dir.mkdir()
    try {
      test(dir)
    } catch {
      case e: Exception => {
        logger.error("erorr occured", e)
        throw e
      }
    } finally {
      FileUtils.deleteDirectory(dir)
    }
  }

  def createFile(dir: File, name: String, content: String, ro: Boolean = false): File = {
    val f = new File(dir, name)
    FileUtils.writeStringToFile(f, content)
    f.setWritable(!ro)
    f
  }

}

