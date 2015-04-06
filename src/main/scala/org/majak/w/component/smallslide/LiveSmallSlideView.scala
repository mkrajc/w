package org.majak.w.component.smallslide

import org.majak.w.component.presentation.PresentationViewProvider
import org.majak.w.rx.{ObservableView, UiEvent}
import rx.lang.scala.Observable

sealed trait LiveSmallSlideUiEvent extends UiEvent

case class StartPresentation(source: PresentationViewProvider) extends LiveSmallSlideUiEvent

case object HidePresentation extends LiveSmallSlideUiEvent

case object BlackScreen extends LiveSmallSlideUiEvent

case object BlackScreenOff extends LiveSmallSlideUiEvent

trait LiveSmallSlideView extends SmallSlideView with ObservableView {
  private lazy val events = createUiEventSubject[LiveSmallSlideUiEvent]


  protected def fireStartPresentation(source: PresentationViewProvider) = {
    setEnableBlackScreenButton(true);
    events.onNext(StartPresentation(source))
  }

  protected def fireStopPresentation() = {
    events.onNext(HidePresentation)
    setEnableBlackScreenButton(false);
  }

  protected def fireBlackScreenOn() = events.onNext(BlackScreen)

  protected def fireBlackScreenOff() = events.onNext(BlackScreenOff)

  protected def setEnableBlackScreenButton(enabled: Boolean)

  lazy val observable: Observable[LiveSmallSlideUiEvent] =
    preventDoubleClicks(events)


}

