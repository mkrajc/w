package org.majak.w

import org.apache.pivot.beans.BXML
import org.apache.pivot.collections.Map
import org.apache.pivot.wtk._
import org.majak.w.di.UiModule
import org.majak.w.ui.pivot.PivotComponent

class WApplication extends Application.Adapter with PivotComponent[Window] with UiModule {

  @BXML
  var menuPanel: FillPane = _

  @BXML
  var contentPanel: FillPane = _
  
   override def startup(display: Display, properties: Map[String, String]) = {
    bindUi
    root open display
  }

  override protected def onUiBind: Unit = {
    menuPanel add wMainMenu.asComponent
    contentPanel add wMainContentContainer

    wMainMenu.liveButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = wMainContentContainer.liveContent
    })

    wMainMenu.songsButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) = wMainContentContainer.songContent
    })
  }
}

object WApplication {
  def main(args: Array[String]) {
    DesktopApplicationContext.main(classOf[WApplication], args)
  }
}