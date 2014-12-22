package org.majak.w.component.slide

import org.majak.w.rx.{ObservableView, UiEvent}
import rx.lang.scala.{Subject, Observable}

case class SlideChanged(snapshot: SlideSnapshot) extends UiEvent

trait SlideView extends ObservableView {
  private lazy val slideSubject = Subject[SlideChanged]()

  protected def slideChanged() = slideSubject.onNext(SlideChanged(snapshot))

  override val observable: Observable[SlideChanged] = slideSubject

  def showContent(content: Content)

  def snapshot: SlideSnapshot

  def adapt(slideSnapshot: SlideSnapshot)

}
