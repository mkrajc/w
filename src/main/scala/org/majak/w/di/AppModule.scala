package org.majak.w.di

import org.majak.w.component.image.{ImageLibrary, ImageLibraryPresenter, ImageLibraryView}
import org.majak.w.component.live.screen.{LiveScreen, LiveScreenPresenter, LiveScreenView}
import org.majak.w.component.main.menu.{MainMenu, MainMenuPresenter, MainMenuView}
import org.majak.w.component.main.screen.{MainScreen, MainScreenPresenter, MainScreenView}
import org.majak.w.component.presentation.PresentationPresenter
import org.majak.w.component.smallslide.preview.{PreviewSmallSlide, PreviewSmallSlidePresenter, PreviewSmallSlideView}
import org.majak.w.component.smallslide.{LiveSmallSlide, LiveSmallSlidePresenter, LiveSmallSlideView}
import org.majak.w.model.song.SongUiModule


trait AppModule extends UiModule with SongUiModule {

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

  lazy val imageLibraryPresenter = doBind[ImageLibraryView, ImageLibraryPresenter](new ImageLibraryPresenter(imageWatchDog), new ImageLibrary)
}
