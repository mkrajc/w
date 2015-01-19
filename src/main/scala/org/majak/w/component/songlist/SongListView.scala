package org.majak.w.component.songlist

import org.majak.w.model.song.data.SongModel.SongListItem
import org.majak.w.rx.{Action, ObservableView, StateViewAction, UiEvent}
import org.majak.w.ui.component.pivot.SearchBoxEvent
import org.majak.w.ui.component.{ListView, Selectable}
import rx.lang.scala.Observable

sealed trait SongListUiEvent extends UiEvent

case class Refresh(action: Action) extends SongListUiEvent

case class SongItemSelected(item: Option[SongListItem]) extends SongListUiEvent

trait SongListView extends ListView[SongListItem] with Selectable[SongListItem] with StateViewAction with ObservableView {

  private lazy val eventSubject = createUiEventSubject[SongListUiEvent]

  def fireRefresh() = eventSubject.onNext(Refresh(this))

  protected def fireSelected() = eventSubject.onNext(SongItemSelected(selected))

  override def observable: Observable[SongListUiEvent] = eventSubject

  def searchBoxObservable: Observable[SearchBoxEvent]

}

trait SongListViewHandler {
  def onSongListItemSelected(item: Option[SongListItem])
}



