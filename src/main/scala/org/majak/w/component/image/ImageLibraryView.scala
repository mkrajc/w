package org.majak.w.component.image

import org.apache.pivot.wtk.media.Image
import org.majak.w.rx.{ObservableView, UiEvent}
import rx.lang.scala.Observable

case object ImageSelected extends UiEvent

trait ImageLibraryView extends ObservableView {


  def addImage(img: Image)

  private lazy val imageSelected = createUiEventSubject

  override def observable: Observable[ImageSelected.type] = preventDoubleClicks(imageSelected)
}
