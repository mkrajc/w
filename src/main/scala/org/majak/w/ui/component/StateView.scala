package org.majak.w.ui.component

sealed trait State

case object OK extends State

case object ERROR extends State

case object LOADING extends State

trait StateView {
  def setState(state: State): Unit

  def stateError() = setState(ERROR)

  def stateLoading() = setState(LOADING)

  def stateOk() = setState(OK)
}
