package org.majak.w.component.live.screen

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.live.slide.TextContent
import org.majak.w.component.live.smallslide.{LiveSmallSlide, LiveSmallSlideView}
import org.majak.w.ui.pivot.PivotComponent


class LiveScreen extends PivotComponent with LiveScreenView {

  @BXML
  protected var fillPane: FillPane = _

  @BXML
  protected var button: PushButton = _

  override def setLiveSmallSlideView(v: LiveSmallSlideView) = {
    val lss: LiveSmallSlide = v.asInstanceOf[LiveSmallSlide]

    fillPane.add(lss.asComponent)

    //todo just for test
    button.setButtonData("show time")
    button.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        lss.slide.showContent(TextContent(List(System.currentTimeMillis() + "")))
    })

  }
}
