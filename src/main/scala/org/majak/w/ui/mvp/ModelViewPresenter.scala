package org.majak.w.ui.mvp

import org.majak.w.ui.event.Messaging

trait View
abstract class Presenter[V <: View] extends Messaging {

  bind

  protected def createViewImpl: V = {new View {}.asInstanceOf[V]}
  protected def onBind(v: View) = {}
  protected final def bind = onBind (view)
  val view: V = createViewImpl
}
