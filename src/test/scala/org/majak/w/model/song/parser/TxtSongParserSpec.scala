package org.majak.w.model.song.parser

import java.io.ByteArrayInputStream

import org.scalatest.{FlatSpec, Matchers}


class TxtSongParserSpec extends FlatSpec with Matchers {
  "TxtSongParser" should "parse if correct format" in {
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
}
