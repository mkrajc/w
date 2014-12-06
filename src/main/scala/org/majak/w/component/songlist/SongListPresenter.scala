package org.majak.w.component.songlist

import org.majak.w.component.songlist.view.SongListView
import org.majak.w.controller.{SongController, SongIndexChangeListener}
import org.majak.w.model.SongModel.SongListItem
import org.majak.w.service.SongService
import org.majak.w.ui.component.pivot.searchbox.SearchHandler
import org.majak.w.ui.mvp.Presenter
import org.majak.w.utils.Utils

class SongListPresenter(view: SongListView,
                        songController: SongController,
                        songService: SongService)
  extends Presenter[SongListView](view) with SongIndexChangeListener {

  var data: List[SongListItem] = _


  override protected def onBind(v: SongListView) = {
    songController addSongIndexChangeListener this
    view.addSearchHandler(h = new SearchHandler {
      override def onSearch(text: String): Unit = {
        val filtered = data filter (s =>
          (Utils normalizeAccentedText s.name).toLowerCase contains Utils.normalizeAccentedText(text).toLowerCase)
        view showData filtered
      }

      override def onSearchCancel(): Unit = view.showData(data)
    })

  }

  override def onSongIndexChanged: Unit = {
    data = songService.getAllSongs map (s => SongListItem(s.id, s.name, s.name))
    view.showData(data)
  }
}