package org.majak.w.component.presentation

import org.majak.w.component.slide._
import org.majak.w.component.smallslide.live._
import org.majak.w.component.smallslide.preview.PreviewSlideConfirmed
import org.majak.w.rx.{ObserverPresenter, UiEvent}
import rx.lang.scala.Observable

/**
 * Managed presentation screen
 */
class PresentationPresenter(liveSmallSlideEvents: Observable[LiveSmallSlideUiEvent],
                            previewSmallSlideEvents: Observable[UiEvent]) extends
ObserverPresenter[PresentationView] {

  val slideAdapter = new SlideAdapter(view.slideView.snapshot.size)

  liveSmallSlideEvents.subscribe(onNext = e => handleLiveSmallSlideEvent(e))
  previewSmallSlideEvents.filter(_ => bound).subscribe(this)


  private var blackScreenBackup: Option[SlideSnapshot] = _

  private def handleLiveSmallSlideEvent(event: LiveSmallSlideUiEvent): Unit = {
    event match {
      case StartPresentation(source) => startPresentation(source)
      case HidePresentation => hidePresentation()
      case BlackScreen => blackScreen()
      case BlackScreenOff => blackScreenOff()
    }
  }

  override def onNext(value: UiEvent): Unit = {
    value match {
      case StartPresentation(source) => startPresentation(source)
      case PreviewSlideConfirmed(slide) => view.slideView.apply(slideAdapter.adapt(slide))
    }
  }

  private def blackScreenOff(): Unit = {
    inBound(() => {
      blackScreenBackup.map(view.slideView.apply)
      blackScreenBackup = None
    })
  }

  private def blackScreen(): Unit = {
    inBound(() => {
      this.blackScreenBackup = Some(view.slideView.snapshot)
      view.slideView.showContent(EmptyBack)
      view.slideView.showContent(EmptyFront)
    })
  }

  def hidePresentation() = {
    inBound(() => {
      blackScreenBackup = None
      view.hide()
      unbind()
    })
  }

  def startPresentation(source: PresentationViewProvider) = {
    if (!bound) {
      this bind source.create
      view.show()
    }
  }

  private def inBound(f: () => Unit): Unit = {
    if (bound) {
      f()
    }
  }

}
