package org.majak.w.component.live.screen

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.image.{ImageLibrary, ImageLibraryView}
import org.majak.w.component.live.song.{SongPanel, SongPanelView}
import org.majak.w.component.smallslide.preview.{PreviewSmallSlide, PreviewSmallSlideView}
import org.majak.w.component.smallslide.{LiveSmallSlide, LiveSmallSlideView}
import org.majak.w.ui.pivot.PivotComponent


class LiveScreen extends PivotComponent with LiveScreenView {

  @BXML var tabPanels: TabPane = _
  @BXML protected var slidePanels: FillPane = _

  override def setSongPanel(songPanelView: SongPanelView) = {
    tabPanels.getTabs.add(songPanelView.asInstanceOf[SongPanel].asComponent)
  }

  override def setImageLibrary(imageLibraryView: ImageLibraryView): Unit = {
    tabPanels.getTabs.add(imageLibraryView.asInstanceOf[ImageLibrary].asComponent)
  }

  override def setLiveSmallSlideView(view: LiveSmallSlideView) = {
    val lss: LiveSmallSlide = view.asInstanceOf[LiveSmallSlide]
    slidePanels.add(lss.asComponent)
  }

  override def setPreviewSmallSlideView(view: PreviewSmallSlideView): Unit = {
    val pss: PreviewSmallSlide = view.asInstanceOf[PreviewSmallSlide]
    slidePanels.add(pss.asComponent)
  }


}
