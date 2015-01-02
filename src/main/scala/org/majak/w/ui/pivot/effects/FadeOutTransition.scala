package org.majak.w.ui.pivot.effects

import org.apache.pivot.wtk.Component

class FadeOutTransition(override val component: Component, override val duration: Int)
  extends FadeInTransition(component, duration) {

  override protected def applyPercentage(percentage: Float): Unit = fadeDecorator.setOpacity(1 - percentage)

}
