package org.majak.w.component.image


import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.apache.pivot.wtk.media.Image
import org.majak.w.ui.pivot.PivotComponent


class ImageLibrary extends PivotComponent with ImageLibraryView {

  @BXML protected var imagePanel: FlowPane = _

  private var imageLoadingIndicator: Option[ActivityIndicator] = Some(createActivityIndicator)

  override protected def onUiBind(): Unit = {
    imagePanel.getStyles.put("alignment", HorizontalAlignment.CENTER)
    imageLoadingIndicator.map(imagePanel.add)
  }

  private def createActivityIndicator: ActivityIndicator = {
    val indicatorSize = 32
    val activityIndicator = new ActivityIndicator
    activityIndicator.setPreferredSize(indicatorSize, indicatorSize)
    activityIndicator.setActive(true)
    activityIndicator
  }

  override def addImage(img: Image): Unit = {
    imageLoadingIndicator.map(f = indicator => {
      imagePanel.remove(indicator)
      imageLoadingIndicator = None
    })
    val b = new Border()
    val iv = new ImageView(img)

    b setContent iv

    b.setPreferredSize(120, 90)
    iv.setPreferredSize(120, 90)
    iv.setCursor(Cursor.HAND)
    iv.getStyles.put("fill", true)

    imagePanel add b
    imagePanel.repaint()
  }
}
