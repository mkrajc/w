package org.majak.w.component.live.screen

import org.majak.w.component.live.song.SongPanelView
import org.majak.w.ui.mvp.View

trait LiveScreenView extends View {
  def setSongPanel(songPanelView: SongPanelView)
}