package org.majak.w.ui.pivot.effects

import org.apache.pivot.wtk.effects.{Transition, TransitionListener}


trait TransitionAutoRemove {

  def transition: Transition

  def startAndRemove(): Unit = {
    startAndRemove(new TransitionListener {
      override def transitionCompleted(transition: Transition): Unit = ()
    })
  }

  def startAndRemove(listener: TransitionListener): Unit = {
    val transitionListener = new TransitionListener() {
      def transitionCompleted(t: Transition) = {
        t.end()
        listener.transitionCompleted(t)
      }
    }

    transition.start(transitionListener)
  }
}
