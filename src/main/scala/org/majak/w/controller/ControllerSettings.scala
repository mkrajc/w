package org.majak.w.controller


trait ControllerSettings {
  val dataDir = """.\data"""
  val imageDir = dataDir + """\images"""
  val appDir = dataDir + """\.app"""
  val indexDir = appDir + """\index"""
  val thumbnailsDir = appDir + """\thumbs"""

}
