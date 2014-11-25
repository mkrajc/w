package org.majak.w.component.songlist

import org.majak.w.component.songlist.pivot.SongListComponent
import org.majak.w.component.songlist.view.SongListView
import org.majak.w.controller.{SongController, SongIndexChangeListener}
import org.majak.w.model.SongListItem
import org.majak.w.service.SongService
import org.majak.w.ui.component.pivot.searchbox.SearchHandler
import org.majak.w.ui.mvp.{Presenter, View}
import org.majak.w.utils.Utils

class SongListPresenter(songController: SongController,
                        songService: SongService)
  extends Presenter[SongListView] with SongIndexChangeListener {

  var data: List[SongListItem] = _

  override protected def createViewImpl: SongListView = new SongListComponent

  override protected def onBind(v: View) = {
    songController addSongIndexChangeListener this
    view.addSearchHandler(new SearchHandler {
      override def onSearch(text: String): Unit = {
        val filtered = data filter (s=>
          Utils.normalizeAccentedText(s.name).toLowerCase contains (Utils.normalizeAccentedText(text).toLowerCase))
        view showData filtered
      }

      override def onSearchCancel(): Unit = view.showData(data)
    })
  }

  override def onSongIndexChanged: Unit =  {
    data = songService.getAllSongs map (s => SongListItem(s.id, s.name, s.name))
    view.showData(data)
  }
}