package org.majak.w.ui.pivot.effects

import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.effects.{FadeDecorator, Transition, TransitionListener}
import org.slf4j.LoggerFactory

class FadeInTransition(val component: Component, val duration: Int)
  extends Transition(duration, 30, false) with TransitionAutoRemove {

  private val logger = LoggerFactory.getLogger(getClass)

  protected val fadeDecorator = new FadeDecorator

  override def stop() = {
    component.getDecorators().remove(fadeDecorator)
    logger.trace("transition stopped on [{}]", component)
    super.stop()
  }

  override def start(transitionListener: TransitionListener) = {
    logger.trace("transition started on [{}]", component)
    component.getDecorators.add(fadeDecorator)
    super.start(transitionListener)
  }

  override def update() = {
    val percentComplete = getPercentComplete
    if (percentComplete < 1.0f) {
      applyPercentage(percentComplete)
      component.repaint
    }
  }

  protected def applyPercentage(percentage: Float): Unit = fadeDecorator.setOpacity(percentage)

  override def transition: Transition = this
}
