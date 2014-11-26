package org.majak.w.component.live.screen

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.live.monitor.LiveSecondaryMonitorManager
import org.majak.w.ui.pivot.PivotComponent


class LiveScreen extends PivotComponent[SplitPane] with LiveScreenView {

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

    button.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = {
        val m = LiveSecondaryMonitorManager.createManager()
        show.setEnabled(m.isDefined)
        hide.setEnabled(m.isDefined)
        if (m.isDefined) {
          text.setText("defined")
          val p = m.get.createSecondaryPresenter(button.getDisplay.getHostWindow)
          show.getButtonPressListeners.add(new ButtonPressListener {
            override def buttonPressed(button: Button) = p.view.show
          })
          hide.getButtonPressListeners.add(new ButtonPressListener {
            override def buttonPressed(button: Button) = p.view.hide
          })
        } else {
          text.setText("undefined")
        }
      }
    })


  }
}
