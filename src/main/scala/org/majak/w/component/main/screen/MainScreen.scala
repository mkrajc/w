package org.majak.w.component.main.screen

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.FillPane
import org.majak.w.component.live.screen.LiveScreenView
import org.majak.w.component.main.menu.{MainMenuUiHandler, MainMenuView}
import org.majak.w.ui.mvp.View
import org.majak.w.ui.pivot.Conversions._
import org.majak.w.ui.pivot.PivotComponent


class MainScreen extends PivotComponent with MainScreenView {

  @BXML
  protected var menuPanel: FillPane = _

  @BXML
  protected var contentPanel: FillPane = _

  protected var liveScreenView: LiveScreenView = _

  override def setMenu(menu: MainMenuView) = {
    menuPanel add toComponent(menu)
    menu.addUiHandler(new MainMenuUiHandler {
      override def onSongsScreenRequested = {}
      override def onLiveScreenRequested = liveContent
    })
  }

  override def setLiveScreen(liveScreen: LiveScreenView) = {
    liveScreenView = liveScreen
    liveContent
  }

  def liveContent = switchContent(liveScreenView)

  def switchContent[V <: View](v: V) = {
    contentPanel.removeAll()
    contentPanel add toComponent(v)
  }
}
