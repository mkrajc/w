package org.majak.w.component.slide


import java.awt.Color

import org.apache.pivot.wtk._
import org.apache.pivot.wtk.media.Image
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

  private val MINIMUM_FONT_SIZE = 1
  private val MINIMUM_PADDING = 10
  private val MAXIMUM_PADDING = 50

  private val DEFAULT_FONT_SIZE = 10

  var front: Front = EmptyFront
  var back: Back = EmptyBack

  private val DEFAULT_SETTINGS = Settings(
    TextSettings(
      FontSettings(DEFAULT_FONT_SIZE, "Arial"),
      HorizontalAlignment.CENTER,
      VerticalAlignment.CENTER),
    ImageSettings(Color.black, stretch = true))

  var settings = DEFAULT_SETTINGS

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
    if (front != textContent) {
      clearTextContent()
      textContent.texts.foreach(addLabel)
      refreshTextLayout()

      front = textContent

      if (effects) {
        labels.foreach(transition)
      }
    }
  }

  private def reloadFront() = {
    front match {
      case current: TextContent =>
        clearTextContent()
        current.texts.foreach(addLabel)
        refreshTextLayout()
        front = current
        if (effects) {
          labels.foreach(transition)
        }
      case EmptyFront => ()
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
    showImage(imageContent, imageContent.img)
  }

  private def showThumbContent(thumbContent: ThumbnailContent) = {
    showImage(thumbContent, thumbContent.thumb.thumbImage)
  }

  private def showImage(b: Back, img: => Image) = {
    if (b != back) {
      clearBackContent()
      addImageView(new ImageView(img))
      autosizeImage()
      refreshImgLayout()

      back = b

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
    // image must be first so it won't cover text
    insert(iv, 0)
  }

  private def addLabel(text: String) = {
    val label = new WLabel(text)
    labels = labels :+ label

    StylesUtils.setColor(label, "#ffffff")
    StylesUtils.applyHorizontalAlignement(label, settings.text.horizontalAlignment)
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
      applySettings(label)

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

  private def refreshImgLayout() = {
    StylesUtils.setBackground(this, StylesUtils.colorToHex(settings.image.background))
    if (imageView.isDefined) {
      imageView.get.getStyles.put("fill", true)
      imageView.get.getStyles.put("preserveAspectRatio", !settings.image.stretch)
    }
  }

  private def computeTextOffset(heights: List[Int]): Int = {
    settings.text.verticalAlignment match {
      case VerticalAlignment.BOTTOM => getSize.height - heights.sum - fontTopBottomPadding
      case VerticalAlignment.CENTER => (getSize.height - heights.sum) / 2
      case VerticalAlignment.TOP => fontTopBottomPadding
    }
  }

  private def autosizeImage() = {
    imageView.foreach(_.setSize(getSize))
  }

  def setHorizontalAlign(ha: HorizontalAlignment) = {
    settings = settings.updateTextSettings(settings.text.setHorizontalAlign(ha))
    labels.foreach(StylesUtils.applyHorizontalAlignement(_, ha))
  }

  def setVerticalAlign(va: VerticalAlignment) = {
    settings = settings.updateTextSettings(settings.text.setVerticalAlign(va))
    refreshTextLayout()
  }

  override def toString: String = "slide@" + hashCode() + " effects=" + effects

  override def snapshot: SlideSnapshot = SlideSnapshot(front, back,
    settings, Size(getSize.width, getSize.height))


  override def apply(slideSnapshot: SlideSnapshot): Unit = {
    if (slideSnapshot != snapshot) {
      settings = slideSnapshot.settings

      showContentInner(slideSnapshot.front)
      showContentInner(slideSnapshot.back)

      setSize(slideSnapshot.size.width, slideSnapshot.size.height)

      refreshTextLayout()
      refreshImgLayout()
      slideChanged()
    }
  }

  def increaseFont(): Unit = {
    val size = settings.text.font.size
    val newSize = size + fontStep(size)
    setFontSize(newSize)
  }

  def decreaseFont(): Unit = {
    val size = settings.text.font.size
    val newSize = math.max(size - fontStep(size), 1)
    setFontSize(newSize)
  }

  def setFontSize(fontSize: Int): Unit = {
    settings = settings.updateFontSettings(settings.text.font.setSize(fontSize))
    logger.info("font size set to " + fontSize)
    reloadFront()
    slideChanged()
  }

  def setFontFamily(family: String): Unit = {
    settings = settings.updateFontSettings(settings.text.font.setFamily(family))

    logger.info("font family changed to to " + family)

    reloadFront()
    slideChanged()
  }

  def setColor(color: Color): Unit = {
    settings = settings.updateImageSettings(settings.image.setBackground(color))
    refreshImgLayout()
    slideChanged()
  }

  def setStretch(stretch: Boolean): Unit = {
    settings = settings.updateImageSettings(settings.image.setStretch(stretch))
    refreshImgLayout()
    slideChanged()
  }

  private def fontStep(size: Int): Int = {
    math.max(MINIMUM_FONT_SIZE, size / 5)
  }

  private def fontLeftRightPadding: Int = {
    val t = math.max(settings.text.font.size, MINIMUM_PADDING)
    math.min(t, MAXIMUM_PADDING)
  }

  private def fontTopBottomPadding: Int = settings.text.font.size / 2

  private def applySettings(label: Label): Unit = {
    StylesUtils.setFontFamily(label, settings.text.font.family)
    StylesUtils.setFontSize(label, settings.text.font.size)
    StylesUtils.applyHorizontalAlignement(label, settings.text.horizontalAlignment)
  }

}