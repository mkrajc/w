package org.majak.w.rx

import org.majak.w.ui.mvp.{Presenter, View}
import rx.lang.scala.{Observable, Observer, Subject}
import scala.concurrent.duration._

trait UiEvent

trait ObservableView extends View {
  protected def createUiEventSubject[E <: UiEvent] = Subject[E]()

  protected def preventDoubleClicks[O <: Observable[UiEvent]](eo: O): Observable[UiEvent] = eo.throttleFirst(750
    .millis)
}

trait ObserverPresenter[V <: View] extends Presenter[V] with Observer[UiEvent]
