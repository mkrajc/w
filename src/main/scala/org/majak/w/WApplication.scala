package org.majak.w

import org.apache.pivot.beans.BXML
import org.apache.pivot.collections.Map
import org.apache.pivot.wtk._
import org.majak.w.di.{UiModule, Module}
import org.majak.w.ui.pivot.{PivotComponent, Binding}

class WApplication extends Application.Adapter with PivotComponent[Window] with UiModule {

  @BXML
  var menuPanel: FillPane = _
  
   override def startup(display: Display, properties: Map[String, String]) = {
    bindUi
    root open display
  }

  override protected def onUiBind: Unit = {
    menuPanel add wMainMenu.asComponent
  }
}

object WApplication {
  def main(args: Array[String]) {
    DesktopApplicationContext.main(classOf[WApplication], args)
  }
}