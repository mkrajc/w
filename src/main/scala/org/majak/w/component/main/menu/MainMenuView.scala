package org.majak.w.component.main.menu

import org.majak.w.ui.mvp.View

trait MainMenuView extends View {
  def addUiHandler(h: MainMenuUiHandler)
}

trait MainMenuUiHandler {
  def onLiveScreenRequested

  def onSongsScreenRequested
}
