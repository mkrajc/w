package org.majak.w.ui.mvp

/**
 * Is marker trait to identify hierarchy of views
 */
trait View

/**
 * Is responsible for managing [[View]]'s
 *
 * @tparam V [[View]] class that is managed by presenter
 */
abstract class Presenter[V <: View] {

  bind

  protected def createViewImpl: V = {new View {}.asInstanceOf[V]}
  protected def onBind(v: View) = {}
  protected final def bind = onBind (view)
  val view: V = createViewImpl
}
