package org.majak.w.component.presentation

import org.majak.w.component.slide._
import org.majak.w.component.smallslide.preview.PreviewSlideConfirmed
import org.majak.w.component.smallslide.{HidePresentation, StartPresentation}
import org.majak.w.rx.{ObserverPresenter, UiEvent}
import org.majak.w.ui.component.Size
import rx.lang.scala.Observable

/**
 * Managed presentation screen
 */
class PresentationPresenter(liveSmallSlideEvents: Observable[UiEvent],
                            previewSmallSlideEvents: Observable[UiEvent]) extends
ObserverPresenter[PresentationView] {

  liveSmallSlideEvents.subscribe(this)
  previewSmallSlideEvents.filter(_ => bound).subscribe(this)

  override def onNext(value: UiEvent): Unit = {
    value match {
      case StartPresentation(source) => startPresentation(source)
      case HidePresentation => hidePresentation()
      case PreviewSlideConfirmed(slide) => view.slideView.apply(adapt(slide))

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

  def adapt(s: SlideSnapshot): SlideSnapshot = {
    SlideSnapshot(s.front: Front, adaptBack(s.back: Back), adaptFontSettings(s.fontSettings, s.size), view.slideView.snapshot.size,
      s.horizontalAlignment, s.verticalAlignment)
  }

  private def adaptFontSettings(fontSettings: FontSettings, size: Size): FontSettings = {
    val ratio: Float = size.height.toFloat / fontSettings.size
    val adaptedSize = math.round(view.slideView.snapshot.size.height / ratio)
    FontSettings(adaptedSize.toInt, fontSettings.family)
  }

  private def adaptBack(back: Back): Back = {
    back match {
      case t: ThumbnailContent =>
        logger.info("Converting thumbnail to image " + t.thumb.source.name)
        ImageContent(t.thumb.loadImage())
      case _ => back
    }
  }

}
