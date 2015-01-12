package org.majak.w.component.image

import org.majak.w.controller.watchdog.image.Thumbnail
import org.majak.w.rx.{ObservableView, UiEvent}
import rx.lang.scala.Observable

sealed trait ImageLibraryUiEvent extends UiEvent

case class ThumbnailClicked(thumbnail: Thumbnail) extends ImageLibraryUiEvent

case object Refresh extends ImageLibraryUiEvent

trait ImageLibraryView extends ObservableView {
  def setThumbSize(size: Int)

  def showThumbnails(imgs: Set[Thumbnail])

  def setEnabled(enabled: Boolean)

  private lazy val eventSubject = createUiEventSubject[ImageLibraryUiEvent]

  protected def fireRefresh() = eventSubject.onNext(Refresh)

  protected def fireThumbnailClicked(thumbnail: Thumbnail) = eventSubject.onNext(new ThumbnailClicked(thumbnail))

  override def observable: Observable[ImageLibraryUiEvent] = preventDoubleClicks(eventSubject)
}
