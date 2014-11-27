package org.majak.w.component.main.menu

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.{Button, ButtonPressListener, PushButton}
import org.majak.w.ui.pivot.PivotComponent

class MainMenu(var uiHandler: MainMenuUiHandler = null) extends PivotComponent with MainMenuView {

  @BXML
  protected var liveButton: PushButton = _

  @BXML
  protected var songsButton: PushButton = _

  override protected def onUiBind: Unit = {
    liveButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = uiHandler.onLiveScreenRequested
    })

    songsButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = uiHandler.onSongsScreenRequested
    })
  }

  override def setUiHandler(h: MainMenuUiHandler): Unit = uiHandler = h
}
