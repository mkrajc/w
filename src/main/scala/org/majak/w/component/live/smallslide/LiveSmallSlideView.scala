package org.majak.w.component.live.smallslide

import org.majak.w.component.presentation.PresentationViewProvider
import org.majak.w.rx.{ObservableView, UiEvent}
import rx.lang.scala.Observable

trait LiveSmallSlideUiEvent extends UiEvent

case class StartPresentation(source: PresentationViewProvider) extends LiveSmallSlideUiEvent

case object HidePresentation extends LiveSmallSlideUiEvent

trait LiveSmallSlideView extends SmallSlideView with ObservableView {
  private lazy val subjStartPresentation = createUiEventSubject[StartPresentation]
  private lazy val subjEndPresentation = createUiEventSubject[HidePresentation.type]

  protected def handleStartPresentation(source: PresentationViewProvider) = subjStartPresentation.onNext(StartPresentation
    (source))

  protected def handleHidePresentation() = subjEndPresentation.onNext(HidePresentation)

  lazy val observable: Observable[UiEvent] =
    preventDoubleClicks(subjStartPresentation).merge(preventDoubleClicks(subjEndPresentation))


}

