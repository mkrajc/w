package org.majak.w.ui.component.common

trait Selectable[T] {
  def select(obj: T)

  def selected: Option[T]
}

package view {

trait ListView[T] {
  def showData(data: List[T])
}

}

package wtk {

import org.apache.pivot.wtk.Component
import org.majak.w.ui.Binding

trait WtkView {
  val asComponent: Component
}

trait WtkComponent[C <: Component] extends WtkView with Binding[C] {
  override def xmlName = "/components/" + getClass.getSimpleName + ".xml"
  val asComponent: C = root
}

}
