package org.majak.w.component.live.monitor

import org.apache.pivot.wtk.{Component, Display, Label, Window}
import org.majak.w.ui.pivot.PivotComponent

class Presentation(val dp: DisplayProvider) extends PivotComponent with PresentationView {

  require(dp != null)

  val window = new Window()


  var display: Display = _

  /**
   * Is called after binding.
   */
  override protected def onUiBind = {
    window.setMaximized(true)
    window.setTitle("presentation")
    window setContent (new Label("TODO"))
  }

  override def show = {
    display = dp.createDisplay
    window open display
  }

  override def hide = {
    window close()
    display.getHostWindow.dispose()
  }

  override val asComponent: Component = window
}

trait DisplayProvider {
  def createDisplay: Display
}
