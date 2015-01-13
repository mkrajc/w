package org.majak.w.controller.watchdog

import java.io.File

case class FileData(path: String, md5hex: String, name: String, ext: String) {
  def toFile = new File(path)
}
