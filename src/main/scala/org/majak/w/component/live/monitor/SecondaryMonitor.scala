package org.majak.w.component.live.monitor

import org.apache.pivot.wtk.{Display, Label, Window}
import org.majak.w.ui.pivot.PivotComponent

/**
 * Created by martin.krajc on 26. 11. 2014.
 */
class SecondaryMonitor(val dp: DisplayProvider) extends PivotComponent[Window] with SecondaryMonitorView {

  require(dp != null)

  var display: Display = _

  /**
   * Is called after binding.
   */
  override protected def onUiBind = {
    asComponent setMaximized true
    asComponent setContent (new Label("TODO"))
  }

  override def show = {
    display = dp.createDisplay
    asComponent open display
  }

  override def hide = {
    asComponent close()
    display.getHostWindow.dispose()
  }
}

trait DisplayProvider {
  def createDisplay: Display

}
