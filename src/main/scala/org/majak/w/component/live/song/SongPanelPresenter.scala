package org.majak.w.component.live.song

import org.majak.w.component.live.smallslide.{PreviewSmallSlidePresenter, LiveSmallSlidePresenter}
import org.majak.w.ui.mvp.Presenter

class SongPanelPresenter(val liveSmallSlidePresenter: LiveSmallSlidePresenter,
                         val previewSmallSlidePresenter: PreviewSmallSlidePresenter,
                         val songDetailPresenter: SongDetailPresenter)
  extends Presenter[SongPanelView] {

  override protected def onBind(v: SongPanelView) = {
    v.setPreviewSmallSlideView(previewSmallSlidePresenter.view)
    v.setLiveSmallSlideView(liveSmallSlidePresenter.view)
    v.setPreviewSongDetailView(songDetailPresenter.view)
  }

}
