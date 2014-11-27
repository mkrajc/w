package org.majak.w.component.live.screen

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.live.monitor.PresentationPresenterFactory
import org.majak.w.ui.pivot.PivotComponent


class LiveScreen extends PivotComponent with LiveScreenView {

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

    val p = PresentationPresenterFactory.createPresentationPresenter(button.getDisplay.getHostWindow)(true)

    button.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = {
          text.setText("defined")

          show.getButtonPressListeners.add(new ButtonPressListener {
            override def buttonPressed(button: Button) = p.view.show
          })
          hide.getButtonPressListeners.add(new ButtonPressListener {
            override def buttonPressed(button: Button) = p.view.hide
          })
      }
    })


  }
}
