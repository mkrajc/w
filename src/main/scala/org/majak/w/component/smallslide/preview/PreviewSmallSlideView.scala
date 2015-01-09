package org.majak.w.component.smallslide.preview

import org.majak.w.component.slide.SlideSnapshot
import org.majak.w.component.smallslide.SmallSlideView
import org.majak.w.rx.{ObservableView, UiEvent}
import rx.lang.scala.Observable

case class PreviewSlideConfirmed(slide: SlideSnapshot) extends UiEvent

trait PreviewSmallSlideView extends SmallSlideView with ObservableView {
  private lazy val subjConfirmed = createUiEventSubject[PreviewSlideConfirmed]

  protected def confirm() = subjConfirmed.onNext(PreviewSlideConfirmed(slideView.snapshot))

  lazy val observable: Observable[PreviewSlideConfirmed] = preventDoubleClicks(subjConfirmed)

}