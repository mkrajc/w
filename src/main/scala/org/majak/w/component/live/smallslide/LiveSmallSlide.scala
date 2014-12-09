package org.majak.w.component.live.smallslide

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.presentation.PivotPresentationViewProvider
import org.majak.w.utils.ListsUtils

class LiveSmallSlide extends SmallSlide with LiveSmallSlideView {

  var uiHandlers:List[LiveSmallSlideUiHandler] = Nil

  @BXML var slidePanel: BoxPane = _

  @BXML var showButton: PushButton = _

  @BXML var hideButton: PushButton = _

  override protected def onUiBind = {

    showButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = {
        val pvp = new PivotPresentationViewProvider(showButton.getDisplay.getHostWindow)
        uiHandlers foreach (_.onStartPresentation(pvp))
      }
    })

    hideButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = uiHandlers foreach (_.onHidePresentation())
    })

//    slide.setPreferredSize(120, 90)

    showButton.setPreferredWidth(25)
    hideButton.setPreferredWidth(25)

    slidePanel add slide

  }

  override def addUiHandler(h: LiveSmallSlideUiHandler) = uiHandlers = ListsUtils.add(h, uiHandlers)
  override def removeUiHandler(h: LiveSmallSlideUiHandler) = uiHandlers = ListsUtils.delete(h, uiHandlers)


}
