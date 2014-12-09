package org.majak.w.component.live.smallslide

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.utils.ListsUtils

class PreviewSmallSlide extends SmallSlide with PreviewSmallSlideView {

  var confirmListeners:List[ConfirmListener] = Nil

  @BXML var slidePanel: BoxPane = _

  override protected def onUiBind = {
    slide.getComponentMouseButtonListeners.add(new ComponentMouseButtonListener.Adapter {
      override def mouseClick(component: Component, button: Mouse.Button, x: Int, y: Int, count: Int): Boolean = {
        if(count >= 2){
          println("double click " + confirmListeners.length)
          confirmListeners.foreach(l=> l(slideView.contents))
        }
        false
      }
    }

    )
    slidePanel add slide

  }

  override def addConfirmListener(h: ConfirmListener) = confirmListeners = ListsUtils.add(h, confirmListeners)
  override def removeConfirmListener(h: ConfirmListener) = confirmListeners = ListsUtils.delete(h, confirmListeners)


}
