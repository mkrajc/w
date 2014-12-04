package org.majak.w.controller

import java.io.File
import java.util.UUID

import org.apache.commons.io.FileUtils
import org.junit.runner.RunWith
import org.majak.w.TestUtils
import org.mockito
import org.mockito.Mockito
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class DirectoryWatchDogSpec extends FlatSpec with Matchers with MockitoSugar{

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

  it should "not notify on rescan when nothing changed" in {

    testInTestDir { f =>
      val h = mock[WatchDogHandler]

      prepareFilesInDir(f, Map("a" -> "a", "b" -> "b"))
      val wd = new DirectoryWatchDog(f)
      wd.addHandler(h)

      val data = wd.scan()
      val index = data.left.get

      wd.rescan(index)

      Mockito.verifyZeroInteractions(h)

    }
  }

  it should "be notified when new files are added to directory" in {

    testInTestDir { f =>
      val h = mock[WatchDogHandler]

      prepareFilesInDir(f, Map("a" -> "a", "b" -> "b"))
      val wd = new DirectoryWatchDog(f)
      wd.addHandler(h)

      val data = wd.scan()
      val index = data.left.get

      prepareFilesInDir(f, Map("c" -> "c", "d" -> "d"))
      wd.rescan(index)

      Mockito.verify(h, Mockito.times(1)).onFilesAdded(mockito.Matchers.any(classOf[Set[FileData]]))

    }
  }

  it should "be notified when some files are deleted from directory" in {

    testInTestDir { f =>
      val h = mock[WatchDogHandler]

      val files = prepareFilesInDir(f, Map("a" -> "a", "b" -> "b"))
      val wd = new DirectoryWatchDog(f)
      wd.addHandler(h)

      val data = wd.scan()
      val index = data.left.get

      files.foreach(FileUtils.deleteQuietly(_))

      wd.rescan(index)

      Mockito.verify(h, Mockito.times(1)).onFilesRemoved(mockito.Matchers.any(classOf[Set[FileData]]))

    }
  }

  def prepareFilesInDir(dir: File, map: Map[String, String]) = {
    map.map(kv => createFile(dir, kv._1, kv._2))
  }

  val tmpDir: File = TestUtils.tempDir

  def testInTestDir(test: File => Unit): Unit = {
    val dir = new File(tmpDir, UUID.randomUUID().toString)
    //  println("creating dir [" + dir.getAbsolutePath + "]")
    dir.mkdir()
    try {
      test(dir)
    } finally {
      FileUtils.deleteDirectory(dir)
      //    println("deleting dir  [" + dir.getAbsolutePath + "]")
    }
  }

  def createFile(dir: File, name: String, content: String, ro: Boolean = false): File = {
    val f = new File(dir, name)
    FileUtils.writeStringToFile(f, content)
    f.setWritable(!ro)
    f
    //      println("created file: " + f)
  }

}

