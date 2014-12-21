package org.majak.w.component.image


import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.apache.pivot.wtk.media.Image
import org.majak.w.ui.pivot.{StylesUtils, PivotComponent}


class ImageLibrary extends PivotComponent with ImageLibraryView {

  @BXML var imagePanel: FlowPane = _

  override def addImage(img: Image): Unit = {
    val b = new Border()
    val iv = new ImageView(img)

    b setContent iv

    b.getComponentMouseListeners.add(new ComponentMouseListener.Adapter {
      override def mouseOver(component: Component): Unit = {
        StylesUtils.setColor(b, "#ee0000")
        StylesUtils.setBackground(iv, "#f3f3f3")
      }

      override def mouseOut(component: Component): Unit = {
        StylesUtils.setColor(b, "#f3f3f3")
        StylesUtils.setBackground(iv, "#ffffff")

      }

    })

    b.setPreferredSize(120, 90)
    iv.setPreferredSize(120, 90)
    iv.setCursor(Cursor.HAND)
    iv.getStyles.put("fill", true)

    imagePanel add b
  }
}
