package org.majak.w.ui.mvp

import org.majak.w.ui.event.Messaging

trait View
abstract class Presenter[V <: View] extends Messaging {
  val view: V = createViewImpl
  protected def createViewImpl: V
}
