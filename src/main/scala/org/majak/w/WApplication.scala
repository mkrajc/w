package org.majak.w

import org.apache.pivot.collections.Map
import org.apache.pivot.wtk._
import org.majak.w.di.UiModule
import org.majak.w.ui.pivot.Conversions._

class WApplication extends Application.Adapter with UiModule {

  override def startup(display: Display, properties: Map[String, String]) = {
    val window = new Window
    window setTitle "w"
    window setMaximized true
    window setContent toComponent(mainScreenPresenter.view)
    window open display
  }
}

object WApplication {
  def main(args: Array[String]) {
    DesktopApplicationContext.main(classOf[WApplication], args)
  }
}