package org.majak.w.component.live.song

import org.majak.w.component.smallslide.LiveSmallSlideView
import org.majak.w.component.smallslide.preview.PreviewSmallSlideView
import org.majak.w.ui.mvp.View

trait SongPanelView extends View {

  def setLiveSmallSlideView(view: LiveSmallSlideView)
  def setPreviewSmallSlideView(view: PreviewSmallSlideView)

  def setPreviewSongDetailView(view: SongDetailView)
}
