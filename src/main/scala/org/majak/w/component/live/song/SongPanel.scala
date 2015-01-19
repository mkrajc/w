package org.majak.w.component.live.song

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.FillPane
import org.majak.w.component.song.detail.{SongDetailView, SongDetail}
import org.majak.w.component.songlist.{SongListComponent, SongListView}
import org.majak.w.ui.pivot.PivotComponent


class SongPanel extends PivotComponent with SongPanelView {

  @BXML protected var songDetailPanels: FillPane = _
  @BXML protected var songListPanel: FillPane = _

  override def setLiveSongDetailView(view: SongDetailView): Unit = {
    songDetailPanels.add(view.asInstanceOf[SongDetail].asComponent)
  }

  override def setSongListView(view: SongListView): Unit = {
    songListPanel.add(view.asInstanceOf[SongListComponent].asComponent)
  }

  override def setPreviewSongDetailView(view: SongDetailView) = {
    songDetailPanels.add(view.asInstanceOf[SongDetail].asComponent)
  }

}
