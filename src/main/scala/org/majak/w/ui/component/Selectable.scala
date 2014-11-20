package org.majak.w.ui.component

trait Selectable[T] {
  def select(obj: T)

  def selected: Option[T]
}



