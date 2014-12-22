package org.majak.w.ui.component.pivot.label

import java.awt.font.{FontRenderContext, GlyphVector, LineMetrics, TextLayout}
import java.awt.geom.{Line2D, Rectangle2D}
import java.awt.image.{BufferedImage, ConvolveOp, Kernel}
import java.awt.{List => AwtList, _}
import java.text.StringCharacterIterator

import org.apache.pivot.collections.Dictionary
import org.apache.pivot.json.JSONSerializer
import org.apache.pivot.serialization.SerializationException
import org.apache.pivot.wtk
import org.apache.pivot.wtk.skin.ComponentSkin
import org.apache.pivot.wtk.{Component, Dimensions, GraphicsUtilities, HorizontalAlignment, Insets, LabelListener, Platform, TextDecoration, Theme, VerticalAlignment}

class WLabelSkin extends ComponentSkin with LabelListener {
  val theme: Theme = Theme.getTheme
  private var font = theme.getFont
  private var color = Color.BLACK
  private var disabledColor = Color.GRAY
  private var backgroundColor: Color = null
  private var textDecoration: TextDecoration = null
  private var horizontalAlignment = HorizontalAlignment.LEFT
  private var verticalAlignment = VerticalAlignment.TOP
  private var padding = Insets.NONE
  private var wrapText: Boolean = false
  private var textHeight: Float = 0

  private var glyphVectors: List[GlyphVector] = Nil

  private def getLabel(): WLabel = getComponent.asInstanceOf[WLabel]

  override def install(component: Component): Unit = {
    super.install(component)
    getLabel().getLabelListeners.add(this)
  }

  def getPreferredWidth(height: Int): Int = {
    val label = getLabel()
    val text: String = label.getText
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
    val label = getLabel()
    val text: String = label.getText
    var preferredHeight: Float = 0
    if (text != null) {
      var widthUpdated: Int = width
      val fontRenderContext: FontRenderContext = Platform.getFontRenderContext
      val lm: LineMetrics = font.getLineMetrics("", fontRenderContext)
      val lineHeight: Float = lm.getHeight
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
    }
    else {
      preferredHeight = 0
    }
    preferredHeight += (padding.top + padding.bottom)

    Math.ceil(preferredHeight).toInt
  }

  override def getPreferredSize: Dimensions = {
    val label = getLabel()
    val text: String = label.getText
    val fontRenderContext: FontRenderContext = Platform.getFontRenderContext
    val lm: LineMetrics = font.getLineMetrics("", fontRenderContext)
    val lineHeight: Int = Math.ceil(lm.getHeight).toInt
    var preferredHeight: Int = 0
    var preferredWidth: Int = 0
    if (text != null && text.length > 0) {
      var str: Array[String] = null
      if (wrapText) {
        str = text.split("\n")
      }
      else {
        str = Array[String](text)
      }
      for (line <- str) {
        val stringBounds: Rectangle2D = font.getStringBounds(line, fontRenderContext)
        val w: Int = Math.ceil(stringBounds.getWidth).toInt
        if (w > preferredWidth) {
          preferredWidth = w
        }
        preferredHeight += lineHeight
      }
    }
    else {
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
    val label = getLabel()
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
    glyphVectors = glyphVector :: glyphVectors
    textHeight +=  glyphVector.getLogicalBounds.getHeight.toFloat
  }

  def paint(graphics: Graphics2D) {
    val label = getLabel()
    val width: Int = getWidth
    val height: Int = getHeight

    if (backgroundColor != null) {
      graphics.setPaint(backgroundColor)
      graphics.fillRect(0, 0, width, height)
    }

    if (glyphVectors.nonEmpty) {
      graphics.setFont(font)

      if (label.isEnabled) graphics.setPaint(color)
      else graphics.setPaint(disabledColor)

      val fontRenderContext = Platform.getFontRenderContext
      val lm = font.getLineMetrics("", fontRenderContext)
      val ascent = lm.getAscent
      val lineHeight = lm.getHeight

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
          /*
          val image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB)
          val g = image.createGraphics()

          g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
          g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON)

          val textLayout = new TextLayout(label.getText, font,g.getFontRenderContext)
          g.setPaint(new Color(128,128,255))
          textLayout.draw(g, x, y + ascent)
          g.dispose()

          val ninth = 1.0f / 9.0f;
          val kernel = Array(ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth)
          val op = new ConvolveOp(new Kernel(3, 3, kernel), ConvolveOp.EDGE_NO_OP, null);
          val image2 = op.filter(image,null);

          */
          graphics.drawGlyphVector(glyphVector, x, y + ascent)
        }

        if (textDecoration != null) {
          graphics.setStroke(new BasicStroke)
          val offset = textDecoration match {
            case TextDecoration.UNDERLINE => y + ascent + 2
            case TextDecoration.STRIKETHROUGH => y + lineHeight / 2 + 1
            case _ => 0
          }

          val line = new Line2D.Float(x, offset, x + lineWidth, offset)

          graphics.draw(line)
        }

        y += textBounds.getHeight.toFloat
      }
    }
  }

  override def isFocusable: Boolean = false

  override def isOpaque: Boolean =
    backgroundColor != null && backgroundColor.getTransparency == Transparency.OPAQUE

  def getFont: Font = font

  def setFont(font: Font) {
    if (font == null) {
      throw new IllegalArgumentException("font is null.")
    }
    this.font = font
    invalidateComponent()
  }

  final def setFont(font: String) {
    if (font == null) {
      throw new IllegalArgumentException("font is null.")
    }
    setFont(LabelSkin.decodeFont(font))
  }

  final def setFont(font: Dictionary[String, _]) {
    if (font == null) {
      throw new IllegalArgumentException("font is null.")
    }
    setFont(Theme.deriveFont(font))
  }

  def getColor: Color = color

  def setColor(color: Color) {
    if (color == null) {
      throw new IllegalArgumentException("color is null.")
    }
    this.color = color
    repaintComponent()
  }

  final def setColor(color: String) {
    if (color == null) {
      throw new IllegalArgumentException("color is null.")
    }
    setColor(GraphicsUtilities.decodeColor(color))
  }

  def getDisabledColor: Color = disabledColor

  def setDisabledColor(color: Color) {
    if (color == null) {
      throw new IllegalArgumentException("color is null.")
    }
    this.disabledColor = color
    repaintComponent()
  }

  final def setDisabledColor(color: String) {
    if (color == null) {
      throw new IllegalArgumentException("color is null.")
    }
    setDisabledColor(GraphicsUtilities.decodeColor(color))
  }

  def getBackgroundColor: Color = backgroundColor

  def setBackgroundColor(backgroundColor: Color) {
    this.backgroundColor = backgroundColor
    repaintComponent()
  }

  final def setBackgroundColor(backgroundColor: String) {
    if (backgroundColor == null) {
      throw new IllegalArgumentException("backgroundColor is null")
    }
    setBackgroundColor(GraphicsUtilities.decodeColor(backgroundColor))
  }

  def getTextDecoration: TextDecoration = textDecoration

  def setTextDecoration(textDecoration: TextDecoration) {
    this.textDecoration = textDecoration
    repaintComponent()
  }

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

  final def setPadding(padding: Dictionary[String, _]) {
    if (padding == null) {
      throw new IllegalArgumentException("padding is null.")
    }
    setPadding(new Insets(padding))
  }


  final def setPadding(padding: Int): Unit = setPadding(new Insets(padding))

  final def setPadding(padding: Number) {
    if (padding == null) {
      throw new IllegalArgumentException("padding is null.")
    }
    setPadding(padding.intValue)
  }

  final def setPadding(padding: String) {
    if (padding == null) {
      throw new IllegalArgumentException("padding is null.")
    }
    setPadding(Insets.decode(padding))
  }

  def getWrapText: Boolean = wrapText

  def setWrapText(wrapText: Boolean): Unit = {
    this.wrapText = wrapText
    invalidateComponent()
  }

  override def textChanged(label: wtk.Label, previousText: String): Unit = invalidateComponent()

  override def maximumLengthChanged(label: wtk.Label, previousMaximumLength: Int): Unit = invalidateComponent()

  def createBlurredText(f: Font, col: Color, str: String, blurIntensity: Int): BufferedImage = {

    val bTemp = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
    val gTemp = bTemp.createGraphics

    gTemp.setFont(f)

    val fm = gTemp.getFontMetrics
    val spread: Int = 8

    val bCBT = new BufferedImage(fm.stringWidth(str) + spread, fm.getFont.getSize + spread, BufferedImage.TYPE_INT_ARGB)
    val gCBT = bCBT.createGraphics.asInstanceOf[Graphics2D]

    gCBT.setColor(col)
    gCBT.setFont(f)
    gCBT.drawString(str, spread / 2, spread / 2 + fm.getFont.getSize)

    val blur: Int = 30 - blurIntensity
    val d: Float = ((.10f / .09f) * (blur + 132) / 160) - 1.0f
    val blurKernel: Array[Float] = Array(1 / 9f - d, 1 / 9f - d, 1 / 9f - d, 1 / 9f - d, 1 / 9f + 8 * d, 1 / 9f - d, 1 / 9f - d, 1 / 9f - d, 1 / 9f - d)
    val cop: ConvolveOp = new ConvolveOp(new Kernel(3, 3, blurKernel), ConvolveOp.EDGE_NO_OP, null)

    gCBT.drawImage(bCBT, cop, 0, 0)

    gTemp.dispose
    return bCBT
  }




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
