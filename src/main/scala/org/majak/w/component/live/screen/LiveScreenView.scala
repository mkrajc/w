package org.majak.w.component.live.screen

import org.majak.w.component.live.smallslide.LiveSmallSlideView
import org.majak.w.ui.mvp.View

trait LiveScreenView extends View {
  def setLiveSmallSlideView(v:LiveSmallSlideView)
}