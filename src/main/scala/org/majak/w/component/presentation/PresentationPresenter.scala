package org.majak.w.component.presentation

import org.majak.w.component.live.smallslide.{HidePresentation, StartPresentation}
import org.majak.w.rx.{ObserverPresenter, UiEvent}
import rx.lang.scala.Observable

/**
 * Managed presentation screen
 */
class PresentationPresenter(liveSmallSlideEvents: Observable[UiEvent]) extends
ObserverPresenter[PresentationView] {

  liveSmallSlideEvents.subscribe(this)

  override def onNext(value: UiEvent): Unit = {
    value match {
      case StartPresentation(source) => startPresentation(source)
      case HidePresentation => hidePresentation()
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
