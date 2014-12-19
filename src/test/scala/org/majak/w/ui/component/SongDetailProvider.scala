package org.majak.w.ui.component

import org.apache.pivot.wtk.Component
import org.majak.w.component.live.song.{SongPartSelected, SongDetail}
import org.majak.w.model.SongModel.SongPart


object SongDetailProvider {
  def createSongDetailTest: Component = {
    val songDetail = new SongDetail
    songDetail.bindView


    val verse1 = SongPart(List("Lord of all creation",
      "of water earth and sky",
      "The heavens are your Tabernacle",
      "Glory to the Lord on high"))

    val chorus = SongPart(List("God of wonders beyond our galaxy",
      "You are Holy, Holy",
      "The universe declares your Majesty",
      "And you are holy holy",
      "Lord of Heaven and Earth",
      "Lord of Heaven and Earth"))

    val verse2 = SongPart(List("Early in the morning",
      "I will celebrate the light",
      "When i stumble in the darkness",
      "I will call your name by night"))

    val bridge = SongPart(List("Lord of heaven and earth",
      "Lord of heaven and earth",
      "Hallelujah to the lord of heaven and earth [repeat 3 times]",
      "holy......holy....holy god....."))


    val list = List(verse1, chorus, verse2, chorus, bridge, chorus)

    songDetail.showSongName("God Of Wonders")
    songDetail.showSongParts(list)

    songDetail.observable.subscribe((sp: SongPartSelected) => println(s"Selected part [${sp.songPart}}]"))

    songDetail.asComponent
  }
}
