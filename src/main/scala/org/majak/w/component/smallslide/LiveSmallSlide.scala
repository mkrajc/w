package org.majak.w.component.smallslide

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.Button.State
import org.apache.pivot.wtk._
import org.majak.w.component.presentation.PivotPresentationViewProvider
import org.majak.w.component.slide.Slide

class LiveSmallSlide extends SmallSlide with LiveSmallSlideView {

  @BXML var slidePanel: BoxPane = _

  @BXML var showButton: PushButton = _

  @BXML var blackScreenButton: PushButton = _

  override protected def onUiBind() = {
    super.onUiBind()

    showButton.setToggleButton(true)
    showButton.getButtonStateListeners.add(new ButtonStateListener {
      override def stateChanged(button: Button, previousState: State): Unit = {
        button.getState match {
          case State.SELECTED => startPresenting()
          case State.UNSELECTED => stopPresenting()
          case _ => ()
        }
      }
    })

    blackScreenButton.setToggleButton(true)
    blackScreenButton.getButtonStateListeners.add(new ButtonStateListener {
      override def stateChanged(button: Button, previousState: State): Unit = {
        button.getState match {
          case State.SELECTED => fireBlackScreenOn()
          case State.UNSELECTED => fireBlackScreenOff()
          case _ => ()
        }
      }
    })

    showButton.setPreferredWidth(25)
    blackScreenButton.setPreferredWidth(25)

  }

  private def startPresenting(): Unit = {
    val pvp = new PivotPresentationViewProvider(showButton.getDisplay.getHostWindow)
    fireStartPresentation(pvp)
  }

  private def stopPresenting(): Unit = {
    fireStopPresentation()
  }

  override protected def setupSlide(slide: Slide): Unit = slidePanel add slide
}
