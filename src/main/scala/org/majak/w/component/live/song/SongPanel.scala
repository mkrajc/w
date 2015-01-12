package org.majak.w.component.live.song

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.FillPane
import org.majak.w.ui.pivot.PivotComponent


class SongPanel extends PivotComponent with SongPanelView {

  @BXML protected var songDetailPanels: FillPane = _



  override def setPreviewSongDetailView(view: SongDetailView) = {
    val songDetail: SongDetail = view.asInstanceOf[SongDetail]
    songDetailPanels.add(songDetail.asComponent)
  }


}
