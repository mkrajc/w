package org.majak.w.ui.mvp

/**
 * Is marker trait to identify hierarchy of views
 */
trait View {
  def bindView
}

/**
 * Is responsible for managing [[View]]'s
 *
 * @tparam V [[View]] class that is managed by presenter
 */
abstract class Presenter[V <: View](val view: V) {

  protected def onBind(v: V) = {}

  final def bind = onBind(view)

}
