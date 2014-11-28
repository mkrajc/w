package org.majak.w.component.live.screen

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.live.smallslide.LiveSmallSlideView
import org.majak.w.ui.pivot.{Conversions, PivotComponent}


class LiveScreen extends PivotComponent with LiveScreenView {

  @BXML
  protected var fillPane: FillPane = _

  override def setLiveSmallSlideView(v: LiveSmallSlideView) = {
    fillPane.add(Conversions.toComponent(v))
  }
}
