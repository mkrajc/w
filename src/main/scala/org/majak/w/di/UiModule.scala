package org.majak.w.di

import java.awt.Window

import org.majak.w.component.live.screen.{LiveScreenView, LiveScreen, LiveScreenPresenter}
import org.majak.w.component.main.menu.{MainMenuView, MainMenu, MainMenuPresenter}
import org.majak.w.component.main.screen.{MainScreenView, MainScreenPresenter, MainScreen}
import org.majak.w.component.presentation.{PresentationPresenterFactory, PresentationPresenter}
import org.majak.w.component.songlist.SongListPresenter
import org.majak.w.component.songlist.pivot.SongListComponent
import org.majak.w.component.songlist.view.SongListView
import org.majak.w.ui.mvp.{View, Presenter}


trait UiModule extends Module {

  lazy val songListPresenter = doBind[SongListView, SongListPresenter](new SongListPresenter(new SongListComponent, songController, songService))
  lazy val mainMenuPresenter = doBind[MainMenuView, MainMenuPresenter](new MainMenuPresenter(new MainMenu))
  lazy val mainScreenPresenter = doBind[MainScreenView, MainScreenPresenter](new MainScreenPresenter(mainMenuPresenter, liveScreenPresenter), new MainScreen())
  lazy val liveScreenPresenter = doBind[LiveScreenView, LiveScreenPresenter](new LiveScreenPresenter(new LiveScreen))

  def presentationPresenter(w: Window): PresentationPresenter = PresentationPresenterFactory.createPresentationPresenter(w)


  private def doBind[V <: View, P <: Presenter[V]](p: P, view: V = null): P = {
    val list = List(view, p.view)
    val viewToBind = list.collectFirst {
      case v if (v != null) => v
    }

    if (viewToBind.isEmpty) throw new IllegalArgumentException("cannot bind presenter with undefined view")
    else p bind viewToBind.get
    p
  }
}
