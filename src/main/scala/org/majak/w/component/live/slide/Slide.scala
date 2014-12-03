package org.majak.w.component.live.slide

import org.apache.pivot.wtk._
import org.apache.pivot.wtk.effects.{Transition, TransitionListener}
import org.apache.pivot.wtk.media.Image
import org.majak.w.ui.pivot.StylesUtils
import test.CollapseTransition


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

  var collapseTransition: CollapseTransition = null

  var textContent: TextContent = _
  var imageContent: TextContent = _

  clearContent()

  private def clearContent() = {
    StylesUtils.setBackground(this, "#000000")
    clearTextContent()
    removeAll()
  }

  private def clearTextContent() = {
    labels.foreach(remove(_))
    labels = Nil
  }

  private def showTextContent() = {
    clearTextContent()
    textContent.texts.foreach(addLabel)
    labels = labels.reverse
    autosizeText()
    labels.foreach(transition)
  }

  private def transition(comp: Component): Unit ={
    if (collapseTransition == null) {
      collapseTransition = new CollapseTransition(comp, 2000, 30)

      val transitionListener = new TransitionListener() {
        def transitionCompleted(transition: Transition) = {
          val collapseTransition = transition.asInstanceOf[CollapseTransition]

          Slide.this.collapseTransition = null
          collapseTransition.end()
        }
      };

      collapseTransition.start(transitionListener)
    }
  }

  def addContent(c: Content) = {
    c match {
      case i: ImageContent => addImageView(new ImageView(i.img))
      case t: TextContent => textContent = t
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

  private def addLabel(text: String) = {
    val label = new Label(text)
    labels = label :: labels

    StylesUtils.setColor(label, "#ffffff")
    StylesUtils.applyHorizontalAlignement(label, horizontalAlignment)
    StylesUtils.applyVerticalAlignement(label, verticalAlignment)
    label.getStyles().put("wrapText", true)

    println("adding label")

    add(label)
  }

  private def computeFontSize() = {
    fontSize = getSize.height / 10
  }

  private def toAccumulatedSum(xs: List[Int]): List[Int] = {
    /*xs.foldLeft((0, List[Int]())) { (pair, h) => {
      (pair._1 + h, (pair._1 + h) :: pair._2)
    }
    }._2.reverse */

    (xs.foldLeft[List[Int]](Nil) { (list, x) => (list.headOption.getOrElse(0) + x) :: list}).reverse

  }


  private def autosizeText() = {
    computeFontSize()

    for (i <- 0 until labels.size) {
      val label = labels(i)
      //apply fontSize first
      StylesUtils.setFontSize(label, fontSize)
      // size as size of slide so can be centered nicely
      label.setWidth(getSize.width)
      // set width limits so prefered height will be computed correctly
      // otherwise it will consider infinite space and would be wrong
      label.setWidthLimits(0, getSize.width)
      label.setHeight(label.getPreferredHeight)
    }

    val heights = 0 :: labels.map(_.getPreferredHeight)
    val liftedH = toAccumulatedSum(heights)

    textOffset = (getSize.height - heights.sum) / 2
    (liftedH, labels).zipped.map { (h, l) => l.setLocation(0, textOffset + h)}

  }

  private def autosizeImage() = {
    Option.apply(imageView).foreach(_.setSize(getSize))
  }

  getComponentListeners.add(new ComponentListener.Adapter {
    override def sizeChanged(component: Component, previousWidth: Int, previousHeight: Int): Unit = {
      autosizeText()
      autosizeImage()
    }

    override def visibleChanged(component: Component) = {
      println(component.isVisible + " visible")
      //showTextContent()
      //transition()
    }

    override def parentChanged(component: Component, previousParent: Container) = {
      println( " parent" + component.getParent)
      if( component.getParent != null) {
        showTextContent()
      }
    }
  })


}

trait Content

case class ImageContent(img: Image) extends Content

case class TextContent(texts: List[String]) extends Content