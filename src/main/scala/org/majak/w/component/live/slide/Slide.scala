package org.majak.w.component.live.slide

import org.apache.pivot.wtk._
import org.apache.pivot.wtk.effects.{Transition, TransitionListener}
import org.majak.w.ui.pivot.StylesUtils
import org.majak.w.ui.pivot.effects.FadeInTransition

import scala.collection.immutable.List
import scala.concurrent.duration._


/**
 * Represent slide on the screen that is able display content
 */
class Slide(val effects: Boolean = false) extends Panel {

  private var imageView: Option[ImageView] = None
  private var labels: List[Label] = Nil

  var fontSize: Int = _
  private var textOffset: Int = _
  private var horizontalAlignment = HorizontalAlignment.CENTER
  private var verticalAlignment = VerticalAlignment.CENTER

  var listeners: List[SlideListener] = Nil

  StylesUtils.setBackground(this, "#000000")
  clearContent()

  getComponentListeners.add(new ComponentListener.Adapter {
    override def sizeChanged(component: Component, previousWidth: Int, previousHeight: Int): Unit = {
      autosizeText()
      autosizeImage()
    }
  })

  def clearContent() = {
    clearTextContent()
    clearImageContent()
    removeAll()
  }

  private def clearTextContent() = {
    labels.foreach(remove(_))
    labels = Nil
  }

  private def clearImageContent() = {
    imageView.foreach(remove(_))
    imageView = None
  }

  private def showTextContent(textContent: TextContent) = {
    clearTextContent()
    textContent.texts.foreach(addLabel)
    autosizeText()

    if (effects) {
      labels.foreach(transition)
    }
  }

  def showContent(c: Content) = {
    System.out.println(s"DEBUG show content [${c}] on  slide [${this}]")
    c match {
      case i: ImageContent => showImageContent(i)
      case t: TextContent => showTextContent(t)
      case _: ClearTextContent => clearTextContent()
      case _: ClearImageContent => clearImageContent()
      case _: ClearContent => clearContent()
    }
    listeners.foreach(_.onContent(c))

  }

  private def showImageContent(imageContent: ImageContent) = {
    clearImageContent()
    addImageView(new ImageView(imageContent.img))
    autosizeImage()

    if (effects) {
      imageView.foreach(transition)
    }
  }

  private def transition(comp: Component): Unit = {
    val t = new FadeInTransition(comp, 1.second.toMillis.toInt, 30)

    val transitionListener = new TransitionListener() {
      def transitionCompleted(transition: Transition) = transition.end
    }

    t.start(transitionListener)

  }

  private def addImageView(iv: ImageView) = {
    imageView = Some(iv)

    iv.getStyles.put("fill", false)
    iv.getStyles.put("preserveAspectRatio", false)

    // image must be first so it won't cover text
    insert(iv, 0)
  }

  private def addLabel(text: String) = {
    val label = new Label(text)
    labels = labels :+ label

    StylesUtils.setColor(label, "#ffffff")
    StylesUtils.applyHorizontalAlignement(label, horizontalAlignment)
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

    textOffset = computeTextOffset(heights)
    (liftedH, labels).zipped.map { (h, l) => l.setLocation(0, textOffset + h)}

  }

  private def computeTextOffset(heights: List[Int]): Int ={
    verticalAlignment match {
      case VerticalAlignment.BOTTOM => (getSize.height - heights.sum)
      case VerticalAlignment.CENTER => (getSize.height - heights.sum) / 2
      case VerticalAlignment.TOP => 0
    }
  }

  private def autosizeImage() = {
    imageView.foreach(_.setSize(getSize))
  }

  def setHorizontalAlign(ha: HorizontalAlignment) = {
    horizontalAlignment = ha
    labels.foreach(StylesUtils.applyHorizontalAlignement(_, ha))
  }

  def setVerticalAlign(va: VerticalAlignment) = {
    verticalAlignment = va
    autosizeText()
  }

  def addSlideListener(l: SlideListener) = listeners = listeners :+ l

}

trait SlideListener {
  def onContent(content: Content)
}
