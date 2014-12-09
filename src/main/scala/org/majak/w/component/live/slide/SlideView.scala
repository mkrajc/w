package org.majak.w.component.live.slide


trait SlideView {
  def showContent(content: Content)
  def contents: List[Content]
  def addSlideContentListener(l: SlideContentListener)
  def removeSlideContentListener(l: SlideContentListener)

  type SlideContentListener = Content => Unit
}
