package org.majak.w.component.presentation

import java.awt.{GraphicsConfiguration, GraphicsDevice, GraphicsEnvironment}

import org.apache.pivot.wtk.DesktopApplicationContext.DisplayListener
import org.apache.pivot.wtk.{Display, DesktopApplicationContext}

case class SecondMonitorDisplayProvider(appWindow: java.awt.Window, gc: GraphicsConfiguration, listener: DisplayListener) extends DisplayProvider {
  override def createDisplay = {
    val bounds = gc.getBounds
    val window = new java.awt.Window(appWindow, gc)
    DesktopApplicationContext.createDisplay(bounds.width, bounds.height, window.getX, window.getY, false, true, true, appWindow, listener)
  }
}

case class SameMonitorProvider(appWindow: java.awt.Window, listener: DisplayListener) extends DisplayProvider {
  override def createDisplay = {
    val window = new java.awt.Window(appWindow, appWindow.getGraphicsConfiguration)
    DesktopApplicationContext.createDisplay(800, 600, window.getX, window.getY, false, true, false,
      appWindow, listener)

  }
}

trait PresentationViewProvider {
  def create: PresentationView
}

class PivotPresentationViewProvider(appWindow: java.awt.Window, onClose: () => Unit) extends PresentationViewProvider {
  override def create = {

    val devices: List[GraphicsDevice] = GraphicsEnvironment.getLocalGraphicsEnvironment.getScreenDevices.toList
    val screenConfigurations = devices map (_.getDefaultConfiguration)
    val secondaryCandidates = screenConfigurations.filter(g => g != appWindow.getGraphicsConfiguration)

    val listener = new DesktopApplicationContext.DisplayListener() {
      override def hostWindowClosed(display: Display): Unit =
        onClose()

      override def hostWindowOpened(display: Display): Unit = {}

    }

    val displayHandler = if (secondaryCandidates.isEmpty) SameMonitorProvider(appWindow, listener)
    else SecondMonitorDisplayProvider(appWindow, secondaryCandidates.head, listener) // take first candidate

    new Presentation(displayHandler)

  }
}

