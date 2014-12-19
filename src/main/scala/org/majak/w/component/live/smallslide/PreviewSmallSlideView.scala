package org.majak.w.component.live.smallslide

import org.majak.w.component.live.slide.Content
import org.majak.w.rx.{UiEvent, ObservableView}
import rx.lang.scala.Observable

case class PreviewSlideConfirmed(contents: List[Content]) extends UiEvent

trait PreviewSmallSlideView extends SmallSlideView with ObservableView {
  private lazy val subjConfirmed = createUiEventSubject[PreviewSlideConfirmed]

  protected def confirm(c: List[Content]) = subjConfirmed.onNext(PreviewSlideConfirmed(c))

  lazy val observable: Observable[PreviewSlideConfirmed] = preventDoubleClicks(subjConfirmed)

}