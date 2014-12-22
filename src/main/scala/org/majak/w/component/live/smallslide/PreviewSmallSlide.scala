package org.majak.w.component.live.smallslide

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._

class PreviewSmallSlide extends SmallSlide with PreviewSmallSlideView {

  @BXML var slidePanel: BoxPane = _

  override protected def onUiBind() = {
    slide.getComponentMouseButtonListeners.add(new ComponentMouseButtonListener.Adapter {
      override def mouseClick(component: Component, button: Mouse.Button, x: Int, y: Int, count: Int): Boolean = {
        if(count >= 2){
          confirm()
        }
        false
      }
    }

    )
    slidePanel add slide

  }
}
