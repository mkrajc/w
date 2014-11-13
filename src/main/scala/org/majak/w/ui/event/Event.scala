package org.majak.w.ui.event

trait Handler {}

abstract class Event[H <: Handler] {
  def dispatch(h: H)
}
