package org.majak.w.component.slide


import org.apache.pivot.wtk._
import org.majak.w.ui.component.Size
import org.majak.w.ui.component.pivot.label.WLabel
import org.majak.w.ui.pivot.StylesUtils
import org.majak.w.ui.pivot.effects.FadeInTransition
import org.slf4j.LoggerFactory

import scala.collection.immutable.List
import scala.concurrent.duration._


/**
 * Represent slide on the screen that is able display content
 */
class Slide(val effects: Boolean = false) extends Panel with SlideView {

  val logger = LoggerFactory.getLogger(getClass)

  private var imageView: Option[ImageView] = None
  private var labels: List[WLabel] = Nil

  private var textOffset: Int = _

  private var horizontalAlignment = HorizontalAlignment.CENTER
  private var verticalAlignment = VerticalAlignment.CENTER

  private val MINIMUM_FONT_SIZE = 1
  private val MINIMUM_PADDING = 10
  private val MAXIMUM_PADDING = 50

  private val DEFAULT_FONT_SIZE = 10

  var front: Front = EmptyFront
  var back: Back = EmptyBack
  var fontSettings = FontSettings(DEFAULT_FONT_SIZE, "Arial")

  StylesUtils.setBackground(this, "#000000")
  clearContent()

  getComponentListeners.add(new ComponentListener.Adapter {
    override def sizeChanged(component: Component, previousWidth: Int, previousHeight: Int): Unit = {
      refreshTextLayout()
      autosizeImage()
    }
  })

  def clearContent() = {
    clearTextContent()
    clearBackContent()
    removeAll()
  }

  private def clearTextContent() = {
    front = EmptyFront
    labels.foreach(remove(_))
    labels = Nil
  }

  private def clearBackContent() = {
    back = EmptyBack
    imageView.foreach(remove(_))
    imageView = None
  }

  private def showTextContent(textContent: TextContent) = {
    if(front != textContent) {
      clearTextContent()
      textContent.texts.foreach(addLabel)
      refreshTextLayout()

      front = textContent

      if (effects) {
        labels.foreach(transition)
      }
    }
  }

  def showContent(c: Content) = {
    showContentInner(c)
    slideChanged()
  }

  private def showContentInner(c: Content) = {
    logger.debug("show content [{}] on slide [{}]", c, this, None)
    c match {
      case i: ImageContent => showImageContent(i)
      case tc: ThumbnailContent => showThumbContent(tc)
      case t: TextContent => showTextContent(t)
      case EmptyFront => clearTextContent()
      case EmptyBack => clearBackContent()
      case Empty => clearContent()
    }
  }

  private def showImageContent(imageContent: ImageContent) = {
    if(imageContent != back) {
      clearBackContent()
      addImageView(new ImageView(imageContent.img))
      autosizeImage()

      back = imageContent

      if (effects) {
        imageView.foreach(transition)
      }
    }
  }

  private def showThumbContent(thumbContent: ThumbnailContent) = {
    if(thumbContent != back) {
      clearBackContent()
      addImageView(new ImageView(thumbContent.thumb.thumbImage))
      autosizeImage()

      back = thumbContent

      if (effects) {
        imageView.foreach(transition)
      }
    }
  }

  private def transition(comp: Component): Unit = {
    val t = new FadeInTransition(comp, 1.second.toMillis.toInt)
    t.startAndRemove()
  }

  private def addImageView(iv: ImageView) = {
    imageView = Some(iv)

    iv.getStyles.put("fill", true)
    iv.getStyles.put("preserveAspectRatio", false)

    // image must be first so it won't cover text
    insert(iv, 0)
  }

  private def addLabel(text: String) = {
    val label = new WLabel(text)
    labels = labels :+ label

    StylesUtils.setColor(label, "#ffffff")
    StylesUtils.applyHorizontalAlignement(label, horizontalAlignment)
    StylesUtils.applyPadding(label, fontLeftRightPadding, 0)

    label.getStyles.put("wrapText", true)

    add(label)
  }

  private def toAccumulatedSum(xs: List[Int]): List[Int] = {
    xs.foldLeft[List[Int]](Nil) { (list, x) => (list.headOption.getOrElse(0) + x) :: list}.reverse
  }

  private def refreshTextLayout() = {

    for (i <- 0 until labels.size) {
      val label = labels(i)
      //apply fontSize first
      applyFontSettings(label)

      // size of label same as size of slide so can be centered nicely
      label.setWidth(getSize.width)
      // set width limits so preferred height will be computed correctly on multilines
      // otherwise it will consider infinite width and would be wrong
      label.setWidthLimits(0, getSize.width)
      label.setHeight(label.getPreferredHeight)
    }

    val heights = 0 :: labels.map(_.getPreferredHeight)
    val liftedH = toAccumulatedSum(heights)

    textOffset = computeTextOffset(heights)
    (liftedH, labels).zipped.map { (h, l) => l.setLocation(0, textOffset + h)}

  }

  private def computeTextOffset(heights: List[Int]): Int = {
    verticalAlignment match {
      case VerticalAlignment.BOTTOM => getSize.height - heights.sum - fontTopBottomPadding
      case VerticalAlignment.CENTER => (getSize.height - heights.sum) / 2
      case VerticalAlignment.TOP => fontTopBottomPadding
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
    refreshTextLayout()
  }


  override def toString: String = "slide@" + hashCode() + " effects=" + effects

  override def snapshot: SlideSnapshot = SlideSnapshot(front, back,
    fontSettings, Size(getSize.width, getSize.height), horizontalAlignment, verticalAlignment)


  override def apply(slideSnapshot: SlideSnapshot): Unit = {
    if (slideSnapshot != snapshot) {

      showContentInner(slideSnapshot.front)
      showContentInner(slideSnapshot.back)
      fontSettings = slideSnapshot.fontSettings
      setSize(slideSnapshot.size.width, slideSnapshot.size.height)

      setHorizontalAlign(slideSnapshot.horizontalAlignment)
      setVerticalAlign(slideSnapshot.verticalAlignment)

      refreshTextLayout()
      slideChanged()
    }
  }

  def increaseFont(): Unit = {
    val size = fontSettings.size
    val newSize = size + fontStep(size)
    fontSettings = fontSettings.setSize(newSize)

    logger.info("font size increased to " + newSize)

    refreshTextLayout()
    slideChanged()
  }

  def setFontFamily(family: String): Unit = {

    fontSettings = fontSettings.setFamily(family)

    logger.info("font family changed to to " + family)

    refreshTextLayout()
    slideChanged()
  }

  def decreaseFont(): Unit = {
    val size = fontSettings.size
    val newSize = math.max(size - fontStep(size), 1)
    fontSettings = fontSettings.setSize(newSize)

    logger.info("font size decreased to " + newSize)

    refreshTextLayout()
    slideChanged()
  }

  private def fontStep(size: Int): Int = {
    math.max(MINIMUM_FONT_SIZE, size / 5)
  }

  private def fontLeftRightPadding: Int = {
    val t = math.max(fontSettings.size, MINIMUM_PADDING)
    math.min(t, MAXIMUM_PADDING)
  }

  private def fontTopBottomPadding: Int = fontSettings.size / 2

  private def applyFontSettings(label: Label): Unit = {
    StylesUtils.setFontFamily(label, fontSettings.family)
    StylesUtils.setFontSize(label, fontSettings.size)
  }

}