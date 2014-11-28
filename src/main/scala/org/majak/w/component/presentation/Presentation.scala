package org.majak.w.component.presentation

import java.awt.Font

import org.apache.pivot.wtk._
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

    val l = new Label("Presentation")
    l.getStyles.put("horizontalAlignment", HorizontalAlignment.CENTER)
    l.getStyles.put("verticalAlignment", VerticalAlignment.CENTER)
    l.getStyles.put("backgroundColor", "#000000")
    l.getStyles.put("color", "#ffffff")
    l.getStyles().put("font", new Font("Arial", Font.BOLD, 80));

    window setContent (l)
  }

  override def show = {
    if(window.isClosed) {
      display = dp.createDisplay
      window open display
    }
  }

  override def hide = {
    if(window.isOpen) {
      window close()
      display.getHostWindow.dispose()
    }
  }

  override val asComponent: Component = window
}

trait DisplayProvider {
  def createDisplay: Display
}
