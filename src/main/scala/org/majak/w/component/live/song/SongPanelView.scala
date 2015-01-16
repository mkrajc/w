package org.majak.w.component.live.song

import org.majak.w.component.songlist.view.SongListView
import org.majak.w.ui.mvp.View

trait SongPanelView extends View {
  def setPreviewSongDetailView(view: SongDetailView)
  def setSongListView(view: SongListView)
}
