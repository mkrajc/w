package org.majak.w.component.live.song

import org.majak.w.component.song.detail.SongDetailView
import org.majak.w.component.songlist.SongListView
import org.majak.w.ui.mvp.View

trait SongPanelView extends View {
  def setPreviewSongDetailView(view: SongDetailView)
  def setLiveSongDetailView(view: SongDetailView)
  def setSongListView(view: SongListView)
}
