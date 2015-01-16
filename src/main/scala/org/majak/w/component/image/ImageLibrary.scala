package org.majak.w.component.image


import java.awt.Color

import org.apache.pivot.beans.BXML
import org.apache.pivot.json.JSON
import org.apache.pivot.wtk.ComponentMouseButtonListener.Adapter
import org.apache.pivot.wtk._
import org.apache.pivot.wtk.effects.ShadeDecorator
import org.majak.w.model.image.data.Thumbnail
import org.majak.w.ui.pivot.{PivotComponent, StylesUtils}


class ImageLibrary extends PivotComponent with ImageLibraryView {

  @BXML protected var imagePanel: FlowPane = _
  @BXML protected var refreshButton: PushButton = _
  @BXML protected var imagesHeader: Label = _

  private val loading = new ShadeDecorator(0.33f, Color.LIGHT_GRAY)

  private var thumbnailSize = 0

  override protected def onUiBind(): Unit = {
    refreshButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button): Unit = fireRefresh()
    })
  }

  override def showThumbnails(imgs: Set[Thumbnail]): Unit = {
    imagesHeader.setText(JSON.get[String](resources, "imageLibrary.header.text").format(imgs.size))
    imagePanel.removeAll()

    imgs.foreach(showThumbnail)

    imagePanel.repaint()
  }

  private def showThumbnail(thumbnail: Thumbnail): Unit = {
    val b = new Border()
    val iv = new ImageView(thumbnail.thumbImage)

    b setContent iv

    b.setPreferredSize(thumbnailSize, thumbnailSize)
    iv.setPreferredSize(thumbnailSize, thumbnailSize)
    StylesUtils.setBackground(iv, "#000000")
    iv.setCursor(Cursor.HAND)
    iv.getStyles.put("fill", true)

    iv.getComponentMouseButtonListeners.add(new Adapter {
      override def mouseClick(component: Component, button: Mouse.Button, x: Int, y: Int, count: Int): Boolean = {
        fireThumbnailClicked(thumbnail)
        false
      }
    })

    imagePanel add b
  }

  override def setThumbSize(size: Int): Unit = thumbnailSize = size

  override def setEnabled(enabled: Boolean): Unit = {
    refreshButton.setEnabled(enabled)
    imagePanel.setEnabled(enabled)

    if (enabled) {
      imagePanel.getDecorators.remove(loading)
    } else {
      imagesHeader.setText(JSON.get(resources, "imageLibrary.header.loading"))
      imagePanel.getDecorators.add(loading)
    }

  }
}
