package org.majak.w.controller

import scala.collection.mutable.ListBuffer

trait SongIndexChangeListener {
  def onSongIndexChanged
}

class SongController {

  private val songIndexChangeListeners = new ListBuffer[SongIndexChangeListener]

  def addSongIndexChangeListener(l: SongIndexChangeListener) = songIndexChangeListeners += l
  def removeSongIndexChangeListener(l: SongIndexChangeListener) = songIndexChangeListeners -= l

  private def notifySongIndexChanged() = songIndexChangeListeners foreach (_.onSongIndexChanged)

  def start = notifySongIndexChanged()

}
