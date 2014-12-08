package org.majak.w.component.live.song

import org.majak.w.component.live.smallslide.LiveSmallSlideView
import org.majak.w.ui.mvp.View

trait SongPanelView extends View {
  def setLiveSmallSlideView(view: LiveSmallSlideView)
  def setPreviewSongDetailView(view: SongDetailView)
}
