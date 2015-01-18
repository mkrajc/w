package org.majak.w.model.song.parser

import java.io.InputStream

import org.majak.w.model.song.data.SongModel.{SongData, SongPart}

import scala.io.Source

trait SongParser {
  def ext: String

  def parse(inputStream: InputStream): Option[SongData] = {
    try {
      parseInputStream(inputStream)
    } finally {
      inputStream.close()
    }
  }

  protected def parseInputStream(inputStream: InputStream): Option[SongData]
}

class TxtSongParser extends SongParser {
  override val ext: String = "txt"

  override def parseInputStream(inputStream: InputStream): Option[SongData] = {
    val lines = Source.fromInputStream(inputStream).getLines().toList
    if (lines.nonEmpty) {
      val name = lines.head.trim
      val parts = groupPrefix(lines.tail)(line => line.isEmpty).map(
        l =>
          SongPart(l.filterNot(_.isEmpty))
      )

      if (name.isEmpty) {
        None
      } else {
        Some(SongData(name = name, parts = parts))
      }
    } else {
      None
    }
  }

  def groupPrefix[T](xs: List[T])(p: T => Boolean): List[List[T]] = xs match {
    case List() => List()
    case x :: xs1 =>
      val (ys, zs) = xs1 span (!p(_))
      (x :: ys) :: groupPrefix(zs)(p)
  }

}
