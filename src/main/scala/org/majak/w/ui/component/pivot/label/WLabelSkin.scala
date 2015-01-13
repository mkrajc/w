package org.majak.w.ui.component.pivot.label

import java.awt._
import java.awt.font.{FontRenderContext, GlyphVector}
import java.awt.geom.Rectangle2D
import java.text.StringCharacterIterator

import org.apache.pivot.collections.Dictionary
import org.apache.pivot.json.JSONSerializer
import org.apache.pivot.serialization.SerializationException
import org.apache.pivot.wtk
import org.apache.pivot.wtk.skin.ComponentSkin
import org.apache.pivot.wtk.{Component, Dimensions, GraphicsUtilities, HorizontalAlignment, Insets, LabelListener, Platform, Theme, VerticalAlignment}

class WLabelSkin extends ComponentSkin with LabelListener {

  val theme: Theme = Theme.getTheme

  private var font = theme.getFont
  private var color = Color.BLACK

  private var horizontalAlignment = HorizontalAlignment.LEFT
  private var verticalAlignment = VerticalAlignment.TOP

  private var padding = Insets.NONE
  private var wrapText: Boolean = false
  private var textHeight: Float = 0

  private val outlineStrokeWidth = 3
  private val outlineColor = Color.BLACK

  private var glyphVectors: scala.collection.immutable.List[GlyphVector] = scala.collection.immutable.Nil

  private def label: WLabel = getComponent.asInstanceOf[WLabel]

  override def install(component: Component): Unit = {
    super.install(component)
    label.getLabelListeners.add(this)
  }

  def getPreferredWidth(height: Int): Int = {
    val text = label.getText
    var preferredWidth: Int = 0

    if (text != null && text.length > 0) {
      val fontRenderContext = Platform.getFontRenderContext

      val str = if (wrapText) text.split("\n")
      else Array[String](text)

      for (line <- str) {
        val stringBounds = font.getStringBounds(line, fontRenderContext)
        val w = math.ceil(stringBounds.getWidth).toInt
        if (w > preferredWidth) {
          preferredWidth = w
        }
      }
    }
    preferredWidth += (padding.left + padding.right)
    preferredWidth
  }

  def getPreferredHeight(width: Int): Int = {
    val text = label.getText
    var preferredHeight: Float = 0
    if (text != null) {

      var widthUpdated: Int = width

      val fontRenderContext = Platform.getFontRenderContext
      val lineHeight = font.getLineMetrics("", fontRenderContext).getHeight

      preferredHeight = lineHeight

      val n: Int = text.length
      if (n > 0 && wrapText && widthUpdated != -1) {

        widthUpdated -= (padding.left + padding.right)

        var lineWidth: Float = 0
        var lastWhitespaceIndex: Int = -1
        var i: Int = 0
        while (i < n) {
          val c: Char = text.charAt(i)
          if (c == '\n') {
            lineWidth = 0
            lastWhitespaceIndex = -1
            preferredHeight += lineHeight
          }
          else {
            if (Character.isWhitespace(c)) {
              lastWhitespaceIndex = i
            }
            val characterBounds: Rectangle2D = font.getStringBounds(text, i, i + 1, fontRenderContext)
            lineWidth += characterBounds.getWidth.toFloat
            if (lineWidth > widthUpdated && lastWhitespaceIndex != -1) {
              i = lastWhitespaceIndex
              lineWidth = 0
              lastWhitespaceIndex = -1
              preferredHeight += lineHeight
            }
          }
          i += 1
        }
      }
    } else {
      preferredHeight = 0
    }

    preferredHeight += (padding.top + padding.bottom)

    math.ceil(preferredHeight).toInt
  }

  override def getPreferredSize: Dimensions = {
    val text = label.getText

    val fontRenderContext = Platform.getFontRenderContext
    val lineHeight: Int = Math.ceil(font.getLineMetrics("", fontRenderContext).getHeight).toInt

    var preferredHeight = 0
    var preferredWidth = 0

    if (text != null && text.length > 0) {
      val str = if (wrapText) {
        text.split("\n")
      } else {
        Array[String](text)
      }

      for (line <- str) {
        val stringBounds = font.getStringBounds(line, fontRenderContext)
        val w = math.ceil(stringBounds.getWidth).toInt

        if (w > preferredWidth) {
          preferredWidth = w
        }
        preferredHeight += lineHeight
      }
    } else {
      preferredHeight += lineHeight
    }

    preferredHeight += (padding.top + padding.bottom)
    preferredWidth += (padding.left + padding.right)

    new Dimensions(preferredWidth, preferredHeight)
  }

  override def getBaseline(width: Int, height: Int): Int = {
    val fontRenderContext = Platform.getFontRenderContext
    val lm = font.getLineMetrics("", fontRenderContext)
    val ascent = lm.getAscent
    val textHeightLocal =
      if (wrapText) math.max(getPreferredHeight(width) - (padding.top + padding.bottom), 0)
      else math.ceil(lm.getHeight).toInt

    verticalAlignment match {
      case VerticalAlignment.TOP => padding.top + ascent.round
      case VerticalAlignment.CENTER => math.round((height - textHeightLocal) / 2 + ascent.round)
      case VerticalAlignment.BOTTOM => math.round(height - (textHeightLocal + padding.bottom) + ascent.round)
      case _ => -1
    }
  }

  def layout(): Unit = {
    val text: String = label.getText
    textHeight = 0

    if (text != null) {
      val n: Int = text.length
      if (n > 0) {
        val fontRenderContext: FontRenderContext = Platform.getFontRenderContext
        if (wrapText) {
          val width: Int = getWidth - (padding.left + padding.right)
          var i: Int = 0
          var start: Int = 0
          var lineWidth: Float = 0
          var lastWhitespaceIndex: Int = -1
          val ci: StringCharacterIterator = new StringCharacterIterator(text)
          while (i < n) {
            val c: Char = text.charAt(i)
            if (c == '\n') {
              appendLine(text, start, i, fontRenderContext)
              start = i + 1
              lineWidth = 0
              lastWhitespaceIndex = -1
            }
            else {
              if (Character.isWhitespace(c)) {
                lastWhitespaceIndex = i
              }
              val characterBounds: Rectangle2D = font.getStringBounds(ci, i, i + 1, fontRenderContext)
              lineWidth += characterBounds.getWidth.toFloat
              if (lineWidth > width && lastWhitespaceIndex != -1) {
                appendLine(text, start, lastWhitespaceIndex, fontRenderContext)
                i = lastWhitespaceIndex
                start = i + 1
                lineWidth = 0
                lastWhitespaceIndex = -1
              }
            }
            i += 1
          }
          appendLine(text, start, i, fontRenderContext)
        }
        else {
          appendLine(text, 0, text.length, fontRenderContext)
        }
      }
    }
  }

  private def appendLine(text: String, start: Int, end: Int, fontRenderContext: FontRenderContext) {
    val line = new StringCharacterIterator(text, start, end, start)
    val glyphVector = font.createGlyphVector(fontRenderContext, line)
    glyphVectors = glyphVectors :+ glyphVector
    textHeight += glyphVector.getLogicalBounds.getHeight.toFloat
  }

  def paint(graphics: Graphics2D) {

    val width: Int = getWidth
    val height: Int = getHeight

    if (glyphVectors.nonEmpty) {
      graphics.setFont(font)

      val fontRenderContext = Platform.getFontRenderContext
      val lm = font.getLineMetrics("", fontRenderContext)
      val ascent = lm.getAscent

      var y = verticalAlignment match {
        case VerticalAlignment.TOP => padding.top
        case VerticalAlignment.BOTTOM => height - (textHeight + padding.bottom)
        case VerticalAlignment.CENTER => (height - textHeight) / 2
      }

      for (glyphVector <- glyphVectors) {

        val textBounds = glyphVector.getLogicalBounds
        val lineWidth = textBounds.getWidth.toFloat

        val x = horizontalAlignment match {
          case HorizontalAlignment.LEFT => padding.left
          case HorizontalAlignment.RIGHT => width - (lineWidth + padding.right)
          case HorizontalAlignment.CENTER => (width - lineWidth) / 2
        }

        if (graphics.isInstanceOf[PrintGraphics]) {
          val text: String = label.getText
          if (text != null && text.length > 0) {
            graphics.drawString(text, x, y + ascent)
          }
        } else {

          graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
          graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
          graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)

          val shape = glyphVector.getOutline(x, y + ascent)

          drawOutline(graphics, shape)
          //drawShadow(graphics, shape)

          graphics.setPaint(color)
          graphics.fill(shape)

          //graphics.drawGlyphVector(glyphVector, x, y + ascent)
        }

        y += textBounds.getHeight.toFloat
      }
    }
  }

  private def drawOutline(graphics: Graphics2D, shape: Shape): Unit = {
    graphics.setComposite(AlphaComposite.Src)
    graphics.setStroke(new BasicStroke(outlineStrokeWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL))
    graphics.setPaint(outlineColor)
    graphics.draw(shape)
  }

  override def isFocusable: Boolean = false

  override def isOpaque: Boolean = true

  def getFont: Font = font

  def setFont(font: Font) {
    if (font == null) {
      throw new IllegalArgumentException("font is null.")
    }
    this.font = font
    invalidateComponent()
  }

  final def setFont(font: String): Unit = setFont(LabelSkin.decodeFont(font))

  final def setFont(font: Dictionary[String, _]): Unit = setFont(Theme.deriveFont(font))

  def getColor: Color = color

  def setColor(color: Color) {
    if (color == null) {
      throw new IllegalArgumentException("color is null.")
    }
    this.color = color
    repaintComponent()
  }

  final def setColor(color: String): Unit = setColor(GraphicsUtilities.decodeColor(color))

  def getHorizontalAlignment: HorizontalAlignment = horizontalAlignment

  def setHorizontalAlignment(horizontalAlignment: HorizontalAlignment) {
    if (horizontalAlignment == null) {
      throw new IllegalArgumentException("horizontalAlignment is null.")
    }
    this.horizontalAlignment = horizontalAlignment
    repaintComponent()
  }

  def getVerticalAlignment: VerticalAlignment = verticalAlignment

  def setVerticalAlignment(verticalAlignment: VerticalAlignment) {
    if (verticalAlignment == null) {
      throw new IllegalArgumentException("verticalAlignment is null.")
    }
    this.verticalAlignment = verticalAlignment
    repaintComponent()
  }

  def getPadding: Insets = padding

  def setPadding(padding: Insets) {
    if (padding == null) {
      throw new IllegalArgumentException("padding is null.")
    }
    this.padding = padding
    invalidateComponent()
  }

  final def setPadding(padding: Dictionary[String, _]): Unit = setPadding(new Insets(padding))

  final def setPadding(padding: Int): Unit = setPadding(new Insets(padding))

  final def setPadding(padding: Number) {
    if (padding == null) {
      throw new IllegalArgumentException("padding is null.")
    }
    setPadding(padding.intValue)
  }

  final def setPadding(padding: String): Unit = setPadding(Insets.decode(padding))

  def getWrapText: Boolean = wrapText

  def setWrapText(wrapText: Boolean): Unit = {
    this.wrapText = wrapText
    invalidateComponent()
  }

  override def textChanged(label: wtk.Label, previousText: String): Unit = invalidateComponent()

  override def maximumLengthChanged(label: wtk.Label, previousMaximumLength: Int): Unit = invalidateComponent()
}

object LabelSkin {
  def decodeFont(value: String): Font = {
    if (value.startsWith("{")) {
      try {
        Theme.deriveFont(JSONSerializer.parseMap(value))
      } catch {
        case e: SerializationException => throw new IllegalArgumentException(e)
      }
    } else {
      Font.decode(value)
    }
  }
}
