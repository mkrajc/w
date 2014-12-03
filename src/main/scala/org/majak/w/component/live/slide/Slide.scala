package org.majak.w.component.live.slide

import java.awt.{Font, Graphics2D}

import org.apache.pivot.wtk._
import org.apache.pivot.wtk.media.Image
import org.apache.pivot.wtk.skin.LabelSkin
import org.majak.w.ui.pivot.StylesUtils

/**
 * Represent slide on the screen that is able display content
 */
class Slide extends Panel {

  var imageView: ImageView = _
  var labels: List[Label] = Nil
  var fontSize: Int = _
  var textOffset: Int = _
  var horizontalAlignment = HorizontalAlignment.CENTER
  var verticalAlignment = VerticalAlignment.CENTER

  StylesUtils.setBackground(this, "#000000")


  def addContent(c: Content) = {
    c match {
      case i: ImageContent => addImageView(new ImageView(i.img))
      case t: TextContent => {
        t.texts.foreach(
        { s =>
          val label = new Label(s)
          labels = label :: labels
          add(label)
        })
      }
    }
  }

  private def addImageView(iv: ImageView) = {
    imageView = iv
    // styles fill=true
    // preserve aspect ratio
    imageView.getStyles.put("fill", true)
    imageView.getStyles.put("preserveAspectRatio", false)

    add(iv)
  }

  private def setLabel(l: Label) = {
    // ll = l
    l.getStyles.put("horizontalAlignment", HorizontalAlignment.CENTER)
    l.getStyles.put("verticalAlignment", VerticalAlignment.CENTER)
    l.getStyles().put("font", new Font("Arial", Font.BOLD, 80))


    // l.getStyles().put("padding", "{top:40, left:30, bottom:40, right:30}")
    //l.setSkin(new TestLabelSkin())
    //l.getStyles.put("backgroundColor", "#343434")
    add(l)
  }

  getComponentListeners.add(new ComponentListener.Adapter {
    override def sizeChanged(component: Component, previousWidth: Int, previousHeight: Int): Unit = {

      fontSize = getSize.height / 10

      for (i <- 0 until labels.size) {
        val label = labels(i)

        //apply styles first
        StylesUtils.setColor(label, "#ffffff")
        StylesUtils.setFontSize(label, fontSize)
        StylesUtils.applyHorizontalAlignement(label, horizontalAlignment)
        StylesUtils.applyVerticalAlignement(label, verticalAlignment)
        label.getStyles().put("wrapText", true)

        // size as size of slide so can be centered nicely
        label.setWidth(getSize.width)
        // set width limits so prefered height will be computed correctly
        // otherwise it will consider infinite space and would be wrong
        label.setWidthLimits(0, getSize.width)

        label.setHeight(label.getPreferredHeight)

        println("After:" + label.getSize)
      }

      val heights = 0 :: labels.map(_.getPreferredHeight)
      val liftedH = heights.foldLeft(List[Int]()){
        (list, h) => (list.sum + h) :: list
      }

      textOffset = (getSize.height - heights.sum) / 2
      (liftedH.reverse, labels).zipped.map { (h, l) => l.setLocation(0, textOffset + h)}
    }
  })
}

trait Content

case class ImageContent(img: Image) extends Content

case class TextContent(texts: List[String]) extends Content

class ShadowLabelSkin extends LabelSkin {
  override def paint(graphics: Graphics2D): Unit = super.paint(graphics)
}

