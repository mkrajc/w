package org.majak.w.component.presentation

import org.apache.pivot.wtk._
import org.majak.w.component.live.slide.{Slide, SlideView, TextContent}
import org.majak.w.ui.component.Size
import org.majak.w.ui.pivot.PivotComponent
import org.majak.w.utils.ListsUtils

class Presentation(val dp: DisplayProvider) extends PivotComponent with PresentationView {

  require(dp != null)

  val window = new Window()

  val presentationSlide = new Slide(true)

  var display: Display = _

  var sizeChangeListeners: List[SizeChangedListener] = Nil

  override protected def onUiBind = {
    window.setMaximized(true)
    window.setTitle("presentation")

    presentationSlide.showContent(TextContent(List("Presentation")))

    window.getComponentListeners.add(new ComponentListener.Adapter {
      override def sizeChanged(component: Component, previousWidth: Int, previousHeight: Int): Unit = {
        sizeChangeListeners.map(_(Size(component.getWidth, component.getHeight)))
      }
    })

    window setContent presentationSlide
  }

  override val slideView: SlideView = presentationSlide

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

      sizeChangeListeners.map(f => f(Size(0,0)))
    }
  }

  override val asComponent: Component = window

  override def addSizeChangedListener(l: SizeChangedListener) = sizeChangeListeners = ListsUtils.add(l, sizeChangeListeners)

  override def removeSizeChangedListener(l: SizeChangedListener) = sizeChangeListeners = ListsUtils.delete(l, sizeChangeListeners)

}

trait DisplayProvider {
  def createDisplay: Display
}
