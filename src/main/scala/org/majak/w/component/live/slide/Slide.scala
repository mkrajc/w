package org.majak.w.component.live.slide

import org.apache.pivot.wtk._
import org.apache.pivot.wtk.effects.{Transition, TransitionListener}
import org.apache.pivot.wtk.media.Image
import org.majak.w.ui.pivot.StylesUtils
import org.majak.w.ui.pivot.effects.FadeInTransition


/**
 * Represent slide on the screen that is able display content
 */
class Slide(val effects: Boolean = false) extends Panel {

  var imageView: Option[ImageView] = None
  var labels: List[Label] = Nil
  var fontSize: Int = _
  var textOffset: Int = _
  var horizontalAlignment = HorizontalAlignment.CENTER
  var verticalAlignment = VerticalAlignment.CENTER



  var textContent: TextContent = _
  var imageContent: ImageContent = _

  clearContent()

  def clearContent() = {
    StylesUtils.setBackground(this, "#000000")
    clearTextContent()
    removeAll()
  }

  def clearTextContent() = {
    labels.foreach(remove(_))
    labels = Nil
  }

  def clearImageContent() = {
    imageView.foreach(remove(_))
    imageView = None
  }

  private def showTextContent() = {
    clearTextContent()
    textContent.texts.foreach(addLabel)
    autosizeText()

    if(effects) {
      labels.foreach(transition)
    }
  }

  private def showImageContent() = {
    clearImageContent()
    addImageView(new ImageView(imageContent.img))
    autosizeImage()

    if(effects) {
      imageView.foreach(transition)
    }
  }

  private def transition(comp: Component): Unit ={
      val t = new FadeInTransition(comp, 2000, 30)

      val transitionListener = new TransitionListener() {
        def transitionCompleted(transition: Transition) = transition.end
      }

    t.start(transitionListener)

  }

  def showContent(c: Content) = {
    c match {
      case i: ImageContent => {
        imageContent = i
        showImageContent()
        }
      case t: TextContent => {
        textContent = t
        showTextContent()
      }
    }
  }

  private def addImageView(iv: ImageView) = {
    imageView = Some(iv)

    iv.getStyles.put("fill", true)
    iv.getStyles.put("preserveAspectRatio", false)

    insert(iv, 0)
  }

  private def addLabel(text: String) = {
    val label = new Label(text)
    labels = labels :+ label

    StylesUtils.setColor(label, "#ffffff")
    StylesUtils.applyHorizontalAlignement(label, horizontalAlignment)
    StylesUtils.applyVerticalAlignement(label, verticalAlignment)
    label.getStyles().put("wrapText", true)

    add(label)
  }

  private def computeFontSize() = {
    fontSize = getSize.height / 10
  }

  private def toAccumulatedSum(xs: List[Int]): List[Int] = {
    (xs.foldLeft[List[Int]](Nil) { (list, x) => (list.headOption.getOrElse(0) + x) :: list}).reverse
  }

  private def autosizeText() = {
    computeFontSize()

    for (i <- 0 until labels.size) {
      val label = labels(i)
      //apply fontSize first
      StylesUtils.setFontSize(label, fontSize)
      // size of label same as size of slide so can be centered nicely
      label.setWidth(getSize.width)
      // set width limits so prefered height will be computed correctly on multilines
      // otherwise it will consider infinite width and would be wrong
      label.setWidthLimits(0, getSize.width)
      label.setHeight(label.getPreferredHeight)
    }

    val heights = 0 :: labels.map(_.getPreferredHeight)
    val liftedH = toAccumulatedSum(heights)

    textOffset = (getSize.height - heights.sum) / 2
    (liftedH, labels).zipped.map { (h, l) => l.setLocation(0, textOffset + h)}

  }

  private def autosizeImage() = {
    imageView.foreach(_.setSize(getSize))
  }

  getComponentListeners.add(new ComponentListener.Adapter {
    override def sizeChanged(component: Component, previousWidth: Int, previousHeight: Int): Unit = {
      autosizeText()
      autosizeImage()
    }
  })

}

trait Content

case class ImageContent(img: Image) extends Content

case class TextContent(texts: List[String]) extends Content