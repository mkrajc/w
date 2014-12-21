package org.majak.w.component.live.slide

import org.majak.w.rx.{UiEvent, ObservableView}
import rx.lang.scala.Observable

trait SlideEvent extends UiEvent

trait SlideView extends ObservableView {
  def showContent(content: Content)
  def contents: List[Content]
  def addSlideContentListener(l: SlideContentListener)
  def removeSlideContentListener(l: SlideContentListener)

  type SlideContentListener = Content => Unit

  protected lazy val slideSubject = createUiEventSubject[SlideEvent]
  override def observable: Observable[SlideEvent] = slideSubject
}
