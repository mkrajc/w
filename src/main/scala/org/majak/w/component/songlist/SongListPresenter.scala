package org.majak.w.component.songlist

import org.majak.w.model.song.controller.SongController
import org.majak.w.model.song.data.SongModel.{Song, SongListItem}
import org.majak.w.model.song.rx.{SongEvent, SongsRefreshed}
import org.majak.w.ui.component.pivot.{CancelSearch, Search, SearchBoxEvent}
import org.majak.w.ui.mvp.Presenter
import org.majak.w.utils.Utils

class SongListPresenter(songController: SongController) extends Presenter[SongListView] {

  var data: List[SongListItem] = _

  override protected def onBind(v: SongListView) = {
    view.searchBoxObservable.subscribe(onNext = e => handleSearchBoxEvent(e))
    songController.observable.subscribe(onNext = e => handleSongEvent(e))
    songController.subscribeSongListEvents(view.observable)

    view.fireRefresh()
  }

  private def handleSongEvent(event: SongEvent) = {
    event match {
      case SongsRefreshed(d) =>
        data = d.map(songToItem)
        view.showData(data)
      case _ => ()
    }
  }

  private def songToItem(song: Song): SongListItem = {
    SongListItem(song.id, song.name, song.name)
  }

  private def handleSearchBoxEvent(event: SearchBoxEvent): Unit = {
    event match {
      case CancelSearch => view.showData(data)
      case Search(text) =>
        val filtered = data filter (s =>
          (Utils normalizeAccentedText s.name).toLowerCase contains Utils.normalizeAccentedText(text).toLowerCase)
        view showData filtered
    }
  }
}
