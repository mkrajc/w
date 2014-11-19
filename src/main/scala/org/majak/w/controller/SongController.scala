package org.majak.w.controller

import scala.collection.mutable.ListBuffer

trait SongIndexChangeListener {
  def onSongIndexChanged
}

class SongController {

  private val songIndexChangeListeners = new ListBuffer[SongIndexChangeListener]

  def +=(any: Any) = any match {
    case h: SongIndexChangeListener => (songIndexChangeListeners += h)
    case _ => throw new UnsupportedOperationException("cannot add listener : " + any)
  }

  private def notifySongIndexChanged() = songIndexChangeListeners foreach (_.onSongIndexChanged)

  def start = notifySongIndexChanged()

}
