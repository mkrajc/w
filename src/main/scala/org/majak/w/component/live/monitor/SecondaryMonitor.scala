package org.majak.w.component.live.monitor

import org.apache.pivot.wtk.{Display, Label, Window}
import org.majak.w.ui.pivot.PivotComponent

/**
 * Created by martin.krajc on 26. 11. 2014.
 */
class SecondaryMonitor(val display: Display) extends PivotComponent[Window] with SecondaryMonitorView {

  require(display != null)

  /**
   * Is called after binding.
   */
  override protected def onUiBind = {
    asComponent setMaximized true
    asComponent setContent (new Label("TODO"))
  }

  override def show = asComponent open display

  override def hide = {
    display.getHostWindow.dispose()
    asComponent close()
  }
}
