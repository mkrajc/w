package org.majak.w.ui.mvp

trait View
abstract class Presenter[V <: View] {

  bind

  protected def createViewImpl: V = {new View {}.asInstanceOf[V]}
  protected def onBind(v: View) = {}
  protected final def bind = onBind (view)
  val view: V = createViewImpl
}
