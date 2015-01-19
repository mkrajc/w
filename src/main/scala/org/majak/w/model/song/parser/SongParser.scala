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
    val groups = groupByEmptyLine(lines)

    if (groups.nonEmpty) {
      val name = groups.head.mkString(" - ").trim
      val parts = groups.tail.map(
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

  def groupByEmptyLine(xs: List[String]): List[List[String]] = {
    def groupByEmptyLineAcc(xs: List[String], acc: List[List[String]]): List[List[String]] = {
      if (xs.isEmpty) {
        acc.reverse
      } else {
        val trimBeginning = xs.dropWhile(_.isEmpty)
        val (ys, left) = trimBeginning.span(!_.isEmpty)
        groupByEmptyLineAcc(left, ys :: acc)
      }
    }

    groupByEmptyLineAcc(xs, Nil)

  }

}
