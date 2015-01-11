package org.majak.w

import java.io.File
import java.util.UUID

import org.apache.commons.io.FileUtils

object TestUtils {
  def tempDir = new File(System.getProperty("java.io.tmpdir"))
}

trait TestableDir {
  val tmpDir: File = TestUtils.tempDir
  def tmpRandFile =  new File(tmpDir, UUID.randomUUID().toString)

  def tmpRandDir =  {
    val t = tmpRandFile
    FileUtils.forceMkdir(t)
    t
  }

  def testInTestDir(directory: File)(test: File => Unit): Unit = {
    val dir = if (directory.exists()) {
      directory
    } else {
      tmpRandFile
    }
    FileUtils.forceMkdir(dir)
    try {
      test(dir)
    } catch {
      case e: Exception =>
        e.printStackTrace()
        throw e
    } finally {
      FileUtils.deleteQuietly(dir)
    }
  }

  def testInTmpDir(test: File => Unit): Unit = {
    testInTestDir(new File(tmpDir, UUID.randomUUID().toString))(test)
  }

  def prepareFilesInDir(dir: File, map: Map[String, String]) = {
    map.map(kv => createFile(dir, kv._1, kv._2))
  }

  def createFile(dir: File, name: String, content: String, ro: Boolean = false): File = {
    val f = new File(dir, name)
    FileUtils.writeStringToFile(f, content)
    f.setWritable(!ro)
    f
  }
}
