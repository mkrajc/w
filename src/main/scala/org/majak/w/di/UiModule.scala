package org.majak.w.di

import org.majak.w.component.live.screen.{LiveScreen, LiveScreenPresenter}
import org.majak.w.component.main.menu.{MainMenu, MainMenuPresenter}
import org.majak.w.component.main.screen.{MainScreen, MainScreenPresenter}
import org.majak.w.component.songlist.SongListPresenter
import org.majak.w.component.songlist.pivot.SongListComponent
import org.majak.w.ui.mvp.{Presenter, View}


trait UiModule extends Module {

  lazy val songListPresenter = bindPresenter(new SongListPresenter(bindView(new SongListComponent), songController, songService))
  lazy val mainMenuPresenter = new MainMenuPresenter(bindView(new MainMenu))
  lazy val mainScreenPresenter = bindPresenter(new MainScreenPresenter(bindView(new MainScreen), mainMenuPresenter, liveScreenPresenter))
  lazy val liveScreenPresenter = new LiveScreenPresenter(bindView(new LiveScreen))

  private def bindView[V <: View](v: V): V = {
    v.bindView
    return v
  }

  private def bindPresenter[P <: Presenter[_]](p: P): P = {
    p.bind
    return p
  }

}
