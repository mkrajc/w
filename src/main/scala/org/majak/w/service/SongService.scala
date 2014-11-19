package org.majak.w.service

import org.majak.w.model.Song

trait SongService {

  def getAllSongs(): List[Song]
}
