package org.majak.w.ui.mvp

import org.majak.w.utils.ListsUtils

/**
 * Is marker trait to identify hierarchy of views
 */
trait View {
  def bindView()

  def unbindView()
}

/**
 * Is responsible for managing [[View]]'s
 *
 * @tparam V [[View]] class that is managed by presenter
 */
abstract class Presenter[V <: View] {

  protected def onBind(v: V) = {}

  protected def onUnbind(v: V) = {}

  private var v: Option[V] = None

  def view: V = if (v.isDefined) v.get else throw new IllegalStateException(s"Presenter [$this] has not bound view")

  protected var bindListeners: List[BindListener] = Nil

  protected[mvp] def bound = v.isDefined

  final def bind(view: V): Unit = {
    val start = System.currentTimeMillis()

    this.v = Some(view)
    view.bindView()

    val endUi = System.currentTimeMillis()
    onBind(view)

    val end = System.currentTimeMillis()
    println("Presenter [" + this + "] bind to view [" + view + "] in "
      + (endUi - start) + "/" + (end - start) + "ms")

    bindListeners.map(listener => listener(view))
  }

  final def unbind(): Unit = {
    def unbindView(v: V) = {
      val start = System.currentTimeMillis()
      v.unbindView()

      val endUi = System.currentTimeMillis()

      onUnbind(v)

      val end = System.currentTimeMillis()
      println("Presenter [" + this + "] unbind view [" + v + "] in "
        + (endUi - start) + "/" + (end - start) + "ms")
    }

    v.map(unbindView(_))
    v = None

  }

  type BindListener = V => Unit

  def addBindListener(l: BindListener) = bindListeners = ListsUtils.add(l, bindListeners)

  def removeBindListener(l: BindListener) = bindListeners = ListsUtils.delete(l, bindListeners)

}
