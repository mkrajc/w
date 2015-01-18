package org.majak.w.component.songlist.view

import org.majak.w.model.song.data.SongModel.SongListItem
import org.majak.w.rx.{ObservableView, UiEvent}
import org.majak.w.ui.component.pivot.searchbox.SearchHandler
import org.majak.w.ui.component.{ListView, Selectable, StateView}
import rx.lang.scala.Observable

sealed trait SongListUiEvent extends UiEvent

case object Refresh extends SongListUiEvent

trait SongListView extends ListView[SongListItem] with Selectable[SongListItem] with StateView with ObservableView {
  def addHandler(h: SongListViewHandler)

  def removeHandler(h: SongListViewHandler)

  def addSearchHandler(h: SearchHandler)

  def removeSearchHandler(h: SearchHandler)

  private lazy val eventSubject = createUiEventSubject[SongListUiEvent]

  protected def fireRefresh() = eventSubject.onNext(Refresh)

  override def observable: Observable[SongListUiEvent] = eventSubject

}

trait SongListViewHandler {
  def onSongListItemSelected(item: Option[SongListItem])
}



