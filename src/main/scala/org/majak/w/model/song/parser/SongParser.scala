package org.majak.w.model.song.parser

import java.io.InputStream

import org.majak.w.model.song.data.SongModel.Song

trait SongParser {
  def ext: String

  def parse(inputStream: InputStream): Song
}
