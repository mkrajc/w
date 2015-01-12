package org.majak.w.ui.pivot.decorators


import org.apache.pivot.wtk._
import org.apache.pivot.wtk.effects.OverlayDecorator


class ActivityDecorator extends OverlayDecorator {

  setOverlay(createActivityIndicator)

  private val SIZE = 32

  private def createActivityIndicator: ActivityIndicator = {
    val indicatorSize = SIZE
    val activityIndicator = new ActivityIndicator
    activityIndicator.setPreferredSize(indicatorSize, indicatorSize)
    activityIndicator.setActive(true)
    activityIndicator
  }
}
