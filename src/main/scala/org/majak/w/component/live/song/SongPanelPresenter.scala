package org.majak.w.component.live.song

import org.majak.w.component.live.slide.{Content, TextContent}
import org.majak.w.component.live.smallslide.{PreviewSmallSlidePresenter, LiveSmallSlidePresenter}
import org.majak.w.ui.mvp.Presenter
import rx.lang.scala.Observable

class SongPanelPresenter(val liveSmallSlidePresenter: LiveSmallSlidePresenter,
                         val previewSmallSlidePresenter: PreviewSmallSlidePresenter,
                         val songDetailPresenter: SongDetailPresenter)
  extends Presenter[SongPanelView] {

  override protected def onBind(v: SongPanelView) = {
    v.setPreviewSmallSlideView(previewSmallSlidePresenter.view)
    v.setLiveSmallSlideView(liveSmallSlidePresenter.view)
    v.setPreviewSongDetailView(songDetailPresenter.view)

    val contentBridge: Observable[Content] = songDetailPresenter.view.observable
      .map(sp => TextContent(sp.songPart.lines))

    contentBridge.subscribe(c => previewSmallSlidePresenter.view.slideView.showContent(c))
  }

}
