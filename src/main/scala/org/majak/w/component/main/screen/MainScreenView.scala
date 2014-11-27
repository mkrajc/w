package org.majak.w.component.main.screen

import org.majak.w.component.live.screen.LiveScreenView
import org.majak.w.component.main.menu.MainMenuView
import org.majak.w.ui.mvp.View

trait MainScreenView extends View {
  def setMenu(menu: MainMenuView)

  def setLiveScreen(liveScreen: LiveScreenView)

}
