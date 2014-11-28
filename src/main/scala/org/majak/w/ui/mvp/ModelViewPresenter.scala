package org.majak.w.ui.mvp

/**
 * Is marker trait to identify hierarchy of views
 */
trait View {
  def bindView
  def unbindView
}

/**
 * Is responsible for managing [[View]]'s
 *
 * @tparam V [[View]] class that is managed by presenter
 */
abstract class Presenter[V <: View](var view: V = null) {

  protected def onBind(v: V) = {}
  protected def onUnbind(v: V) = {}

  protected var bound = false

  final def bind(view: V): Unit = {
    val start = System.currentTimeMillis()
    this.view = view
    view.bindView
    val endUi = System.currentTimeMillis()
    onBind(view)
    val end = System.currentTimeMillis()
    println("Presenter [" + this + "] bind to view [" + view + "] in "
      + (endUi - start) + "/" + (end - start) + "ms")
    bound = true
  }

  final def unbind: Unit = {
    val start = System.currentTimeMillis()
    view.unbindView
    val endUi = System.currentTimeMillis()
    onUnbind(view)
    val end = System.currentTimeMillis()
    println("Presenter [" + this + "] unbind view [" + view + "] in "
      + (endUi - start) + "/" + (end - start) + "ms")
    bound = false
  }

}
