package org.majak.w.component.live.monitor

import java.awt.{GraphicsDevice, GraphicsEnvironment}

import org.apache.pivot.wtk.DesktopApplicationContext


class LiveSecondaryMonitorManager(devices: List[GraphicsDevice]) {

  def createSecondaryPresenter(appWindow: java.awt.Window): SecondaryMonitorPresenter = {
    val screenConfigurations = devices map (_.getDefaultConfiguration())
    val gc = screenConfigurations.filter(g => g != appWindow.getGraphicsConfiguration())(0)
    val bounds = gc.getBounds()
    val w = new java.awt.Window(appWindow, gc)

    val display = DesktopApplicationContext.createDisplay(bounds.width, bounds.height, w.getX, w.getY, false, true, true, appWindow, null)

    val view = new SecondaryMonitor(display)
    view.bindView
    val presenter = new SecondaryMonitorPresenter(view)
    presenter.bind

    presenter
  }


}

object LiveSecondaryMonitorManager {

  def createManager(): Option[LiveSecondaryMonitorManager] = {
    val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
    val devices = ge.getScreenDevices
    if (devices.length == 1) None
    else Some(new LiveSecondaryMonitorManager(devices.toList))
  }


}
