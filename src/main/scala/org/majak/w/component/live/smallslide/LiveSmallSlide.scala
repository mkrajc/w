package org.majak.w.component.live.smallslide

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.presentation.PivotPresentationViewProvider
import org.majak.w.ui.pivot.StylesUtils
import scala.collection.mutable.ListBuffer

class LiveSmallSlide extends SmallSlide with LiveSmallSlideView {
  val uiHandlers = new ListBuffer[LiveSmallSlideUiHandler]

  @BXML
  var slidePanel: FillPane = _

  @BXML
  var showButton: PushButton = _

  @BXML
  var hideButton: PushButton = _

  override protected def onUiBind = {

    showButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = {
        val pvp = new PivotPresentationViewProvider(showButton.getDisplay.getHostWindow)
        uiHandlers foreach (_.onStartPresentation(pvp))
      }
    })

    hideButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = uiHandlers foreach (_.onHidePresentation)
    })

    // todo until slide component will be delivered
    val slideBorder = new Border
    val slide: Label = new Label("120x90")
    StylesUtils.centerHorizontal(slide)
    StylesUtils.centerVertical(slide)
    StylesUtils.setBackground(slide, "#d3d30d")
    slide.setPreferredSize(120, 90)
    slideBorder setContent slide

    showButton.setPreferredWidth(25)
    hideButton.setPreferredWidth(25)

    slidePanel add slideBorder

    StylesUtils.setBackground(slidePanel, "#dddd00")

  }

  override def addUiHandler(h: LiveSmallSlideUiHandler) = uiHandlers += h

}
