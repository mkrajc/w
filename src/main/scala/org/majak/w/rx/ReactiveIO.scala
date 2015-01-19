package org.majak.w.rx

import org.majak.w.ui.component.StateView
import org.majak.w.ui.mvp.{Presenter, View}
import rx.lang.scala.{Observable, Observer, Subject}

import scala.concurrent.duration._

trait Event

case object Done extends Event

trait UiEvent extends Event

trait Action {
  def onStart()

  def onEnd()

  def onFailure(fail: Throwable)
}

trait StateViewAction extends StateView with Action {

  override def onEnd(): Unit = stateOk()

  override def onStart(): Unit = stateLoading()

  override def onFailure(fail: Throwable): Unit = stateError()
}

trait ObservableObject extends ObservableCreator {

  protected def preventDoubleClicks[T <: Event](observable: Observable[T]): Observable[T] = observable.throttleFirst(750
    .millis)

  def observable: Observable[Event]
}

trait ObservableCreator {
  protected def createEventSubject[E <: Event] = Subject[E]()
}

trait ObservableView extends View {
  protected def createUiEventSubject[E <: UiEvent] = Subject[E]()

  protected def preventDoubleClicks[T <: UiEvent](observable: Observable[T]): Observable[T] =
    observable.throttleFirst(750
      .millis)

  def observable: Observable[UiEvent]
}

trait ObserverPresenter[V <: View] extends Presenter[V] with Observer[UiEvent]
