package org.majak.w.component.presentation

import java.awt.{GraphicsConfiguration, GraphicsDevice, GraphicsEnvironment}

import org.apache.pivot.wtk.DesktopApplicationContext

case class SecondMonitorDisplayProvider(appWindow: java.awt.Window, gc: GraphicsConfiguration) extends DisplayProvider {
  override def createDisplay = {
    val bounds = gc.getBounds()
    val window = new java.awt.Window(appWindow, gc)
    DesktopApplicationContext.createDisplay(bounds.width, bounds.height, window.getX, window.getY, false, true, true, appWindow, null)
  }
}

case class SameMonitorProvider(appWindow: java.awt.Window) extends DisplayProvider {
  override def createDisplay = {
    val window = new java.awt.Window(appWindow, appWindow.getGraphicsConfiguration)
    DesktopApplicationContext.createDisplay(800, 600, window.getX, window.getY, false, true, false, appWindow, null)
  }
}

trait PresentationViewProvider {
  def create: PresentationView
}

class PivotPresentationViewProvider(appWindow: java.awt.Window) extends PresentationViewProvider {
  override def create = {

    val devices: List[GraphicsDevice] = (GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()).toList
    val screenConfigurations = devices map (_.getDefaultConfiguration())
    val secondaryCandidates = screenConfigurations.filter(g => g != appWindow.getGraphicsConfiguration())

    val displayHandler = if (secondaryCandidates.isEmpty) SameMonitorProvider(appWindow)
    else SecondMonitorDisplayProvider(appWindow, secondaryCandidates(0)) // take first candidate

    new Presentation(displayHandler)

  }
}

