package org.majak.w.component.live.screen

import org.majak.w.component.image.{ImageLibraryPresenter, ThumbnailClicked}
import org.majak.w.component.live.song.SongPanelPresenter
import org.majak.w.component.slide.{Content, TextContent, ThumbnailContent}
import org.majak.w.component.smallslide.live.LiveSmallSlidePresenter
import org.majak.w.component.smallslide.preview.PreviewSmallSlidePresenter
import org.majak.w.component.song.detail.SongDetailPresenter
import org.majak.w.ui.mvp.Presenter
import rx.lang.scala.Observable


class LiveScreenPresenter(val liveSmallSlidePresenter: LiveSmallSlidePresenter,
                          val previewSmallSlidePresenter: PreviewSmallSlidePresenter,
                          val songPanelPresenter: SongPanelPresenter,
                          val songDetailPresenter: SongDetailPresenter,
                          val imageLibraryPresenter: ImageLibraryPresenter) extends Presenter[LiveScreenView] {

  override protected def onBind(v: LiveScreenView) = {
    v.setSongPanel(songPanelPresenter.view)
    v.setPreviewSmallSlideView(previewSmallSlidePresenter.view)
    v.setLiveSmallSlideView(liveSmallSlidePresenter.view)
    v.setImageLibrary(imageLibraryPresenter.view)

    val textContentObservable: Observable[Content] = songDetailPresenter.view.observable
      .map(sp => TextContent(sp.songPart.lines))
    textContentObservable.subscribe(c => previewSmallSlidePresenter.view.slideView.showContent(c))

    imageLibraryPresenter.imagesObservable.subscribe(e => {
      e match {
        case ThumbnailClicked(t) => previewSmallSlidePresenter.view.slideView.showContent(ThumbnailContent(t))
        case _ => ()
      }
    })
  }

}
