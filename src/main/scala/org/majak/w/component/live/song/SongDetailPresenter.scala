package org.majak.w.component.live.song

import org.majak.w.model.SongModel.Song
import org.majak.w.ui.mvp.Presenter

/**
 * Represents whole song on screen
 */
class SongDetailPresenter extends Presenter[SongDetailView]{

   def song(s:Song) = {
    view.showSongName(s.name)
    view.showSongParts(s.parts)
  }


}
