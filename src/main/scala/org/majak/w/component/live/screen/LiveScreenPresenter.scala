package org.majak.w.component.live.screen

import org.majak.w.component.live.song.SongPanelPresenter
import org.majak.w.ui.mvp.Presenter


class LiveScreenPresenter(val songPanelPresenter: SongPanelPresenter) extends Presenter[LiveScreenView] {

  override protected def onBind(v: LiveScreenView) = {
    v.setSongPanel(songPanelPresenter.view)
  }

}
