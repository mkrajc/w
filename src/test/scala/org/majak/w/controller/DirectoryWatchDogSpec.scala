package org.majak.w.controller

import java.io.File
import java.util.UUID

import org.apache.commons.io.FileUtils
import org.junit.runner.RunWith
import org.majak.w.TestUtils
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class DirectoryWatchDogSpec extends FlatSpec with Matchers {

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

  def prepareFilesInDir(dir: File, map: Map[String, String]) = {
    map.foreach(kv => createFile(dir, kv._1, kv._2))
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

  def createFile(dir: File, name: String, content: String, ro: Boolean = false): Unit = {
    val f = new File(dir, name)
    FileUtils.writeStringToFile(f, content)
    f.setWritable(!ro)
    //      println("created file: " + f)
  }

}

