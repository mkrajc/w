package org.majak.w.component.songlist.view

import org.majak.w.model.SongModel.SongListItem
import org.majak.w.ui.component.pivot.searchbox.SearchHandler
import org.majak.w.ui.component.{ListView, Selectable}
import org.majak.w.ui.mvp.View

trait SongListView extends ListView[SongListItem] with Selectable[SongListItem] with View {
  def addHandler(h: SongListViewHandler)
  def removeHandler(h: SongListViewHandler)
  def addSearchHandler(h: SearchHandler)
  def removeSearchHandler(h: SearchHandler)
}

trait SongListViewHandler {
  def onSongListItemSelected(item: Option[SongListItem])
}



