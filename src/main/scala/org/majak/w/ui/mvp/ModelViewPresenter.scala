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
trait Presenter[V <: View] {

  lazy  val view: V = createViewImpl

  protected def createViewImpl: V
  protected def onBind(v: View) = {}

  final def bind = onBind(view)


}
