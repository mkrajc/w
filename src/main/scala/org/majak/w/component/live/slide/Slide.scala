package org.majak.w.component.live.slide

import java.awt.{Font, Graphics2D}

import org.apache.pivot.wtk._
import org.apache.pivot.wtk.media.Image
import org.apache.pivot.wtk.skin.LabelSkin
import test.TLabel

/**
 * Represent slide on the screen that is able display content
 */
class Slide extends Panel {

  var ll: Label = _
  var imageView: ImageView = _

  def addContent(c: Content) = {
    c match {
      case i: ImageContent => addImageView(new ImageView(i.img))
      case t: TextContent => addLabel(new TLabel(t.text))
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

  private def addLabel(l: Label) = {
    ll = l
    l.getStyles.put("horizontalAlignment", HorizontalAlignment.CENTER)
    l.getStyles.put("verticalAlignment", VerticalAlignment.CENTER)
    l.getStyles().put("font", new Font("Arial", Font.BOLD, 80));
    l.getStyles().put("padding", "{top:40, left:30, bottom:40, right:30}");

    //l.setSkin(new TestLabelSkin())
    //l.getStyles.put("backgroundColor", "#343434")
    add(l)
  }

  getComponentListeners.add(new ComponentListener.Adapter {
    override def sizeChanged(component: Component, previousWidth: Int, previousHeight: Int): Unit = {
      println(ll.getSize)
      ll.setSize(getSize)
     // imageView.setSize(getSize)
      println("size of slide " + getSize)


    }


  })

}

trait Content

case class ImageContent(img: Image) extends Content

case class TextContent(text: String) extends Content

class ShadowLabelSkin extends LabelSkin {
  override def paint(graphics: Graphics2D): Unit = super.paint(graphics)
}

