package org.majak.w.di

import org.majak.w.controller.SongController
import org.majak.w.service.LocalSongService
import org.majak.w.ui.component.songlist.SongListPresenter

trait Module {
  val songController = new SongController
  val songService = new LocalSongService
  val songListPresenter = new SongListPresenter(songController, songService)
}
