package org.majak.w

import org.apache.pivot.collections.Map
import org.apache.pivot.wtk._
import org.majak.w.component.live.screen.LiveScreenUiHandler
import org.majak.w.component.presentation.PresentationPresenter
import org.majak.w.di.UiModule
import org.majak.w.ui.pivot.Conversions._

class WApplication extends Application.Adapter with UiModule {

  override def startup(display: Display, properties: Map[String, String]) = {


    val window = new Window
    window setTitle "w"
    window setMaximized true

    window setContent toComponent(mainScreenPresenter.view)
    window open display

    liveScreenPresenter.view.addUiHandler(new LiveScreenUiHandler {
      var presenter: Option[PresentationPresenter] = None
      override def onShow = {
        presenter = if(presenter.isEmpty) Some(presentationPresenter(display.getHostWindow)) else presenter
        presenter.get.view.show
      }

      override def onHide = {
        presenter.foreach(_.view.hide)
        presenter = None
      }
    })
  }
}

object WApplication {
  def main(args: Array[String]) {
    DesktopApplicationContext.main(classOf[WApplication], args)
  }
}