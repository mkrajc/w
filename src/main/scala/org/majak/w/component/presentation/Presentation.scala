package org.majak.w.component.presentation

import org.apache.pivot.wtk._
import org.majak.w.component.live.slide.{Content, SlideListener, TextContent, Slide}
import org.majak.w.ui.pivot.PivotComponent

class Presentation(val dp: DisplayProvider) extends PivotComponent with PresentationView {

  require(dp != null)

  val window = new Window()

  val presentationSlide = new Slide(true)

  var display: Display = _

  /**
   * Is called after binding.
   */
  override protected def onUiBind = {
    window.setMaximized(true)
    window.setTitle("presentation")

    presentationSlide.showContent(TextContent(List("Presentation")))

    window setContent presentationSlide
  }

  override def show() = {
    if (window.isClosed) {
      display = dp.createDisplay
      window open display
    }
  }

  override def hide() = {
    if (window.isOpen) {
      window close()
      display.getHostWindow.dispose()
    }
  }

  override val asComponent: Component = window

  override def onContent(content: Content) = presentationSlide showContent content

  override def addSlideListener(slideListener: SlideListener) = presentationSlide.addSlideListener(slideListener)
}

trait DisplayProvider {
  def createDisplay: Display
}
