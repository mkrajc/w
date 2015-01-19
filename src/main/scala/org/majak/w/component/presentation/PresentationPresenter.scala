package org.majak.w.component.presentation

import org.majak.w.component.slide._
import org.majak.w.component.smallslide.preview.PreviewSlideConfirmed
import org.majak.w.component.smallslide.{HidePresentation, StartPresentation}
import org.majak.w.rx.{ObserverPresenter, UiEvent}
import rx.lang.scala.Observable

/**
 * Managed presentation screen
 */
class PresentationPresenter(liveSmallSlideEvents: Observable[UiEvent],
                            previewSmallSlideEvents: Observable[UiEvent]) extends
ObserverPresenter[PresentationView] {

  val slideAdapter = new SlideAdapter(view.slideView.snapshot.size)

  liveSmallSlideEvents.subscribe(this)
  previewSmallSlideEvents.filter(_ => bound).subscribe(this)


  override def onNext(value: UiEvent): Unit = {
    value match {
      case StartPresentation(source) => startPresentation(source)
      case HidePresentation => hidePresentation()
      case PreviewSlideConfirmed(slide) => view.slideView.apply(slideAdapter.adapt(slide))
    }
  }

  def hidePresentation() = {
    if (bound) {
      view.hide()
      unbind()
    }
  }

  def startPresentation(source: PresentationViewProvider) = {
    if (!bound) {
      this bind source.create
      view.show()
    }
  }


}
