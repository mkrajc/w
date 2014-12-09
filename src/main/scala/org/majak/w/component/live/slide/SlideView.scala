package org.majak.w.component.live.slide


trait SlideView {
  def showContent(content: Content)
  def addSlideContentListener(l: SlideContentListener)
  def removeSlideContentListener(l: SlideContentListener)

  type SlideContentListener = Content => Unit
}
