package org.majak.w.di

import org.majak.w.component.songlist.SongListPresenter
import org.majak.w.controller.SongController
import org.majak.w.service.SongService

trait Module {
  val songController = new SongController
  def songService: SongService = null
  val songListPresenter = {
    val p = new SongListPresenter(songController, songService)
    p.bind
    p
  }
}
