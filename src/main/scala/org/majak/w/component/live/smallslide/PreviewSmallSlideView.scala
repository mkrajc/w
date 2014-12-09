package org.majak.w.component.live.smallslide

import org.majak.w.component.live.slide.Content

trait PreviewSmallSlideView  extends SmallSlideView {
  // def addUiHandler(h: PreviewSmallSlideUiHandler)
  // def removeUiHandler(h: PreviewSmallSlideUiHandler)
  type ConfirmListener = List[Content] => Unit

  def addConfirmListener(l:ConfirmListener)
  def removeConfirmListener(l:ConfirmListener)

}

trait PreviewSmallSlideUiHandler {
}