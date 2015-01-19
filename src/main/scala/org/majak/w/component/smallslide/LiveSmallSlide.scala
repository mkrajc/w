package org.majak.w.component.smallslide

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.presentation.PivotPresentationViewProvider
import org.majak.w.component.slide.Slide

class LiveSmallSlide extends SmallSlide with LiveSmallSlideView {

  @BXML var slidePanel: BoxPane = _

  @BXML var showButton: PushButton = _

  @BXML var hideButton: PushButton = _

  override protected def onUiBind() = {
    super.onUiBind()

    showButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = {
        val pvp = new PivotPresentationViewProvider(showButton.getDisplay.getHostWindow)
        handleStartPresentation(pvp)
      }
    })

    hideButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = handleHidePresentation()
    })

    showButton.setPreferredWidth(25)
    hideButton.setPreferredWidth(25)

  }

  override protected def setupSlide(slide: Slide): Unit = slidePanel add slide
}
