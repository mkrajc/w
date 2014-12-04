package org.majak.w

import java.io.File

object TestUtils {
  def tempDir = new File(System.getProperty("java.io.tmpdir"))
}
