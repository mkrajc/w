package org.majak.w.component.live.screen

import org.majak.w.ui.mvp.View

trait LiveScreenView extends View {
  def addUiHandler(h: LiveScreenUiHandler)
}

trait LiveScreenUiHandler {
  def onShow
  def onHide
}
