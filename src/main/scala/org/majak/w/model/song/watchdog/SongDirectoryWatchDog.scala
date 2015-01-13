package org.majak.w.model.song.watchdog

import java.io.File

import org.majak.w.controller.watchdog._
import org.majak.w.model.song.parser.SongParser

class SongDirectoryWatchDog (root: File, val parsers: List[SongParser]) extends DirectoryWatchDog(root) {

  override val indexName: String = "songs"
  override val indexFile: File = new File(indexDir, indexName)

  override val supportedExtensions: Set[String] = parsers.map(_.ext).toSet

}