package org.majak.w.component.slide

import org.majak.w.rx.{ObservableView, UiEvent}
import rx.lang.scala.{Observable, Subject}

case class SlideChanged(snapshot: SlideSnapshot) extends UiEvent

trait SlideView extends ObservableView {
  private lazy val slideSubject = Subject[SlideChanged]()

  protected def slideChanged() = slideSubject.onNext(SlideChanged(snapshot))

  override val observable: Observable[SlideChanged] = slideSubject

  def showContent(content: Content)

  def snapshot: SlideSnapshot

  /**
   * Adapting snapshot to current slide. It means that font will be sized proportionally to current size of slide
   * @param slideSnapshot
   */
  def adapt(slideSnapshot: SlideSnapshot)

  /**
   * Apply snapshot to current slide. Slide will setup according to snapshot values
   * @param slideSnapshot
   */
  def apply(slideSnapshot: SlideSnapshot)

}
