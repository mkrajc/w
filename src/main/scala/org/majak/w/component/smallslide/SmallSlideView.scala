package org.majak.w.component.smallslide

import org.majak.w.component.slide.SlideView
import org.majak.w.ui.component.Size
import org.majak.w.ui.mvp.View

trait SmallSlideView extends View {
  def slideView: SlideView

  def autoSizeSlideView(size: Size)
}