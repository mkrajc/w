package org.majak.w.component.main.screen

import org.majak.w.component.live.screen.LiveScreenPresenter
import org.majak.w.component.main.menu.MainMenuPresenter
import org.majak.w.ui.mvp.Presenter

class MainScreenPresenter(menuPresenter: MainMenuPresenter,
                          liveScreenPresenter: LiveScreenPresenter) extends Presenter[MainScreenView] {

  override protected def onBind(v: MainScreenView) = {
    v.setMenu(menuPresenter.view)
    v.setLiveScreen(liveScreenPresenter.view)
  }

}
