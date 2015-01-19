package org.majak.w.component.song.detail

import org.majak.w.model.song.controller.SongController

/**
 * Represents whole song on screen
 */
class SongDetailPresenter(songController: SongController) extends AbstractSongDetailPresenter {

  override protected def onBind(v: SongDetailView): Unit = {
    songController.observable.subscribe(onNext = e => handleSongEvent(e))
  }

}
