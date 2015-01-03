package org.majak.w.rx

import org.majak.w.ui.mvp.{Presenter, View}
import rx.lang.scala.{Observable, Observer, Subject}
import scala.concurrent.duration._

trait Event

trait UiEvent extends Event

trait ObservableObject {
  protected def createUiEventSubject[E <: Event] = Subject[E]()

  protected def preventDoubleClicks[T <: Event](observable: Observable[T]): Observable[T] = observable.throttleFirst(750
    .millis)

  def observable: Observable[Event]
}

trait ObservableView extends View {
  protected def createUiEventSubject[E <: UiEvent] = Subject[E]()

  protected def preventDoubleClicks[T <: UiEvent](observable: Observable[T]): Observable[T] =
    observable.throttleFirst(750
      .millis)

  def observable: Observable[UiEvent]
}

trait ObserverPresenter[V <: View] extends Presenter[V] with Observer[UiEvent]
