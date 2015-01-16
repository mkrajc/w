package org.majak.w.di

import org.majak.w.component.image.{ImageLibrary, ImageLibraryPresenter, ImageLibraryView}
import org.majak.w.component.live.screen.{LiveScreen, LiveScreenPresenter, LiveScreenView}
import org.majak.w.component.live.song._
import org.majak.w.component.main.menu.{MainMenu, MainMenuPresenter, MainMenuView}
import org.majak.w.component.main.screen.{MainScreen, MainScreenPresenter, MainScreenView}
import org.majak.w.component.presentation.PresentationPresenter
import org.majak.w.component.smallslide._
import org.majak.w.component.smallslide.preview.{PreviewSmallSlide, PreviewSmallSlidePresenter, PreviewSmallSlideView}
import org.majak.w.component.songlist.SongListPresenter
import org.majak.w.component.songlist.pivot.SongListComponent
import org.majak.w.component.songlist.view.SongListView
import org.majak.w.ui.mvp.{Presenter, View}


trait UiModule extends Module {

  lazy val songListPresenter =
    doBind[SongListView, SongListPresenter](new SongListPresenter(loadSongsTask), new SongListComponent)
  lazy val mainMenuPresenter = doBind[MainMenuView, MainMenuPresenter](new MainMenuPresenter, new MainMenu)
  lazy val mainScreenPresenter = doBind[MainScreenView, MainScreenPresenter](new MainScreenPresenter(mainMenuPresenter, liveScreenPresenter), new MainScreen())

  lazy val presentationPresenter = new PresentationPresenter(liveSmallSlideView.observable, previewSmallSlideView.observable)

  lazy val liveSmallSlideView = new LiveSmallSlide
  lazy val liveSmallSlidePresenter = doBind[LiveSmallSlideView, LiveSmallSlidePresenter](new LiveSmallSlidePresenter(presentationPresenter), liveSmallSlideView)

  lazy val previewSmallSlideView = new PreviewSmallSlide
  lazy val previewSmallSlidePresenter = doBind[PreviewSmallSlideView, PreviewSmallSlidePresenter](new PreviewSmallSlidePresenter(presentationPresenter), previewSmallSlideView)

  lazy val liveScreenPresenter = doBind[LiveScreenView, LiveScreenPresenter](
    new LiveScreenPresenter(liveSmallSlidePresenter,
      previewSmallSlidePresenter,
      songPanelPresenter,
      songDetailPresenter,
      imageLibraryPresenter), new LiveScreen)

  lazy val songDetailView = new SongDetail
  lazy val songDetailPresenter = doBind[SongDetailView, SongDetailPresenter](new SongDetailPresenter, songDetailView)

  lazy val songPanelPresenter = doBind[SongPanelView, SongPanelPresenter](
    new SongPanelPresenter(songDetailPresenter, songListPresenter),
    new SongPanel)

  lazy val imageLibraryPresenter = doBind[ImageLibraryView, ImageLibraryPresenter](new ImageLibraryPresenter(imageWatchDog), new ImageLibrary)

  private def doBind[V <: View, P <: Presenter[V]](p: P, view: V): P = {
    if (view == null) throw new IllegalArgumentException("cannot bind presenter with undefined view")
    else p bind view
    p
  }
}
