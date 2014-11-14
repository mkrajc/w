package org.majak.w.di

import org.majak.w.ui.component.songlist.SongListPresenter

trait PresenterModule extends Module {
  lazy val songListPresenter = new SongListPresenter
}
