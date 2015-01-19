package org.majak.w.model.song.parser

import java.io.ByteArrayInputStream

import org.majak.w.model.song.data.SongModel.SongPart
import org.scalatest.{FlatSpec, Matchers}


class TxtSongParserSpec extends FlatSpec with Matchers {
  it should "parse if file is in correct format" in {
    val parser = new TxtSongParser

    val song =
      """|One
        |
        |I can't remember anything
        |Can't tell if this is true or dream
        |Deep down inside I feel to scream
        |This terrible silence stops me
        |
        |Now that the war is through with me
        |I'm waking up, I cannot see
        |That there is not much left of me
        |Nothing is real but pain now
        |
        |Hold my breath as I wish for death
        |Oh please, God, wake me
        |""".stripMargin

    val songInput = new ByteArrayInputStream(song.getBytes("UTF-8"))
    val parsedSong = parser.parse(songInput)

    parsedSong shouldNot be(null)
    parsedSong should be ('defined)
    parsedSong.get.name shouldEqual "One"
    parsedSong.get.parts.size shouldEqual 3
    parsedSong.get.parts(0).lines(0) shouldEqual "I can't remember anything"
    parsedSong.get.parts(2).lines(1) shouldEqual "Oh please, God, wake me"

  }

  it should "parse if file has empty lines at the beginning" in {
    val parser = new TxtSongParser

    val song = "\n\nOne\n\n\n"

    val songInput = new ByteArrayInputStream(song.getBytes("UTF-8"))
    val parsedSong = parser.parse(songInput)

    parsedSong shouldNot be(null)
    parsedSong should be ('defined)
    parsedSong.get.name shouldEqual "One"
    }

  it should "parse if file has more empty lines between song parts" in {
    val parser = new TxtSongParser

    val song = "\n\nOne\n\n\npart1\n\n\npart2\n\n"

    val songInput = new ByteArrayInputStream(song.getBytes("UTF-8"))
    val parsedSong = parser.parse(songInput)

    parsedSong shouldNot be(null)
    parsedSong should be ('defined)
    parsedSong.get.name shouldEqual "One"
    parsedSong.get.parts(0) shouldEqual SongPart(List("part1"))
    parsedSong.get.parts(1) shouldEqual SongPart(List("part2"))
  }

  it should "parse multiline name" in {
    val parser = new TxtSongParser

    val song = "\n\nOne\nTest\n\n"

    val songInput = new ByteArrayInputStream(song.getBytes("UTF-8"))
    val parsedSong = parser.parse(songInput)

    parsedSong shouldNot be(null)
    parsedSong should be ('defined)
    parsedSong.get.name shouldEqual "One - Test"
  }
}
