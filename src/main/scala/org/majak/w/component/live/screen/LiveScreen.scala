package org.majak.w.component.live.screen

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.live.song.{SongPanel, SongPanelView}
import org.majak.w.ui.pivot.PivotComponent


class LiveScreen extends PivotComponent with LiveScreenView {

  @BXML var liveContentPane: TabPane = _

  @BXML protected var button: PushButton = _

  override protected def onUiBind = {

  }

  override def setSongPanel(songPanelView: SongPanelView) = {
    liveContentPane.getTabs.add(songPanelView.asInstanceOf[SongPanel].asComponent)
  }
}
