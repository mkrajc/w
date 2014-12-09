package org.majak.w.component.live.song

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.{Component, ComponentListener, FillPane}
import org.majak.w.component.live.smallslide.{LiveSmallSlide, LiveSmallSlideView}
import org.majak.w.ui.pivot.PivotComponent


class SongPanel extends PivotComponent with SongPanelView {

  @BXML protected var slidePanels: FillPane = _
  @BXML protected var songDetailPanels: FillPane = _

  override protected def onUiBind: Unit = {
    slidePanels.getComponentListeners.add(new ComponentListener.Adapter {
      override def sizeChanged(component: Component, previousWidth: Int, previousHeight: Int): Unit = {
        ()
        // TODO
      }
    })
  }

  def setLiveSmallSlideView(view: LiveSmallSlideView) = {
    val lss: LiveSmallSlide = view.asInstanceOf[LiveSmallSlide]
    slidePanels.add(lss.asComponent)

    val x= new LiveSmallSlide
    x.bindView
    slidePanels.add(x.asComponent)

  }

  override def setPreviewSongDetailView(view: SongDetailView) = {
    val songDetail: SongDetail = view.asInstanceOf[SongDetail]
    songDetailPanels.add(songDetail.asComponent)
  }
}
