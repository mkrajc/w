package org.majak.w.component.live.song

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.FillPane
import org.majak.w.component.live.smallslide.{LiveSmallSlide, LiveSmallSlideView, PreviewSmallSlide, PreviewSmallSlideView}
import org.majak.w.ui.pivot.PivotComponent


class SongPanel extends PivotComponent with SongPanelView {

  @BXML protected var slidePanels: FillPane = _
  @BXML protected var songDetailPanels: FillPane = _

  override def setLiveSmallSlideView(view: LiveSmallSlideView) = {
    val lss: LiveSmallSlide = view.asInstanceOf[LiveSmallSlide]
    slidePanels.add(lss.asComponent)
  }

  override def setPreviewSongDetailView(view: SongDetailView) = {
    val songDetail: SongDetail = view.asInstanceOf[SongDetail]
    songDetailPanels.add(songDetail.asComponent)
  }

  override def setPreviewSmallSlideView(view: PreviewSmallSlideView): Unit = {
    val pss: PreviewSmallSlide = view.asInstanceOf[PreviewSmallSlide]
    slidePanels.add(pss.asComponent)
  }
}
