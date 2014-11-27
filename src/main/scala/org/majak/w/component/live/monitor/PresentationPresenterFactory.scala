package org.majak.w.component.live.monitor

import java.awt.{GraphicsConfiguration, GraphicsDevice, GraphicsEnvironment}

import org.apache.pivot.wtk.DesktopApplicationContext


object PresentationPresenterFactory {

  def createPresentationPresenter(appWindow: java.awt.Window) : PresentationPresenter = {
    val devices: List[GraphicsDevice] = (GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()).toList
    val screenConfigurations = devices map (_.getDefaultConfiguration())
    val secondaryCandidates = screenConfigurations.filter(g => g != appWindow.getGraphicsConfiguration())

    val displayHandler = if (secondaryCandidates.isEmpty) SameMonitorProvider(appWindow)
    else SecondMonitorDisplayProvider(appWindow, secondaryCandidates(0)) // take first candidate

    //bind peresnter and view
    val view = new Presentation(displayHandler)
    val presenter = new PresentationPresenter(view)
    presenter bind view

    presenter
  }
}

case class SecondMonitorDisplayProvider(appWindow: java.awt.Window, gc: GraphicsConfiguration) extends DisplayProvider {
  override def createDisplay = {
    val bounds = gc.getBounds()
    val window = new java.awt.Window(appWindow, gc)
    DesktopApplicationContext.createDisplay(bounds.width, bounds.height, window.getX, window.getY, false, true, true, appWindow, null)
  }
}

case class SameMonitorProvider(appWindow: java.awt.Window) extends DisplayProvider {
  override def createDisplay = {
    // todo size of window
    val bounds = appWindow.getGraphicsConfiguration.getBounds()
    val window = new java.awt.Window(appWindow, appWindow.getGraphicsConfiguration)
    DesktopApplicationContext.createDisplay(bounds.width, bounds.height, window.getX, window.getY, false, false, false, appWindow, null)
  }
}

