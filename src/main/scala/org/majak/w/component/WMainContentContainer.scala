package org.majak.w.component

import org.apache.pivot.wtk.{FillPane, Component, Label}

class WMainContentContainer extends FillPane {

  val liveComponent: Component = new Label("live content")
  val songComponent: Component = new Label("song content")

  // by default
  liveContent

  def liveContent = switchContent(liveComponent)

  def songContent = switchContent(songComponent)

  def switchContent(c: Component) = {
    removeAll()
    this add c
  }

}
