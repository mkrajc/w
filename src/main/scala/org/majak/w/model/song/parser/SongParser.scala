package org.majak.w.model.song.parser

import java.io.InputStream

import org.majak.w.model.song.data.SongModel.{SongPart, Song}

import scala.io.Source

trait SongParser {
  def ext: String

  def parse(inputStream: InputStream): Song
}

class TxtSongParser extends SongParser {
  override def ext: String = "txt"

  override def parse(inputStream: InputStream): Song = {
    val lines = Source.fromInputStream(inputStream).getLines().toList
    if (lines.nonEmpty) {
      val name = lines.head.trim
      val parts = groupPrefix(lines.tail)(line => line.isEmpty).map(
        l =>
        SongPart(l.filterNot(_.isEmpty))
      )

      Song(name = name, parts = parts, id = 3)
    } else {
      null
    }
  }

  def groupPrefix[T](xs: List[T])(p: T => Boolean): List[List[T]] = xs match {
    case List() => List()
    case x :: xs1 =>
      val (ys, zs) = xs1 span (!p(_))
      (x :: ys) :: groupPrefix(zs)(p)
  }

}
