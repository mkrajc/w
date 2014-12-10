package org.majak.w.ui.pivot.effects

import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.effects.{FadeDecorator, Transition, TransitionListener}
import org.slf4j.LoggerFactory

class FadeInTransition(val component: Component, val duration: Int, val rate: Int) extends Transition(duration, rate, false) {
  val logger = LoggerFactory.getLogger(getClass)

  private val fadeDecorator = new FadeDecorator

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
      val d = getDuration
      fadeDecorator.setOpacity(percentComplete)
      component.repaint
    }
  }
}
