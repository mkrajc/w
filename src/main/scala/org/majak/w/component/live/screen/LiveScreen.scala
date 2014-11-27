package org.majak.w.component.live.screen

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.ui.pivot.PivotComponent

import scala.collection.mutable.ListBuffer


class LiveScreen extends PivotComponent with LiveScreenView {

  val uiHandlers = new ListBuffer[LiveScreenUiHandler]

  @BXML
  protected var button: PushButton = _

  @BXML
  protected var show: PushButton = _

  @BXML
  protected var hide: PushButton = _

  @BXML
  protected var text: Label = _

  /**
   * Is called after binding.
   */
  override protected def onUiBind = {
    button.setButtonData("refresh")
    show.setButtonData("show")
    hide.setButtonData("hide")

    show.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = uiHandlers.foreach(_.onShow)
    })

    hide.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = uiHandlers.foreach(_.onHide)
    })

  }

  override def addUiHandler(h: LiveScreenUiHandler) = uiHandlers += h

}
