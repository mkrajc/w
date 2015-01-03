package org.majak.w.ui.component.pivot

import java.awt.{Color, Font, GraphicsEnvironment}

import org.apache.pivot.wtk._
import org.apache.pivot.wtk.content.{ButtonData, ButtonDataRenderer}
import org.majak.w.ui.pivot.StylesUtils

object FontListButton {
  val FONT_SIZE = 12
}

class FontListButton extends ListButton {

  val fontCandidates  = GraphicsEnvironment.getLocalGraphicsEnvironment.getAllFonts

  //TODO select fonts carefully
  val fonts = fontCandidates.groupBy(_.getFamily).map(_._2.head).toList.sortBy(_.getFamily)

  fonts.foreach(f => {
    getListData.asInstanceOf[org.apache.pivot.collections.List[Font]].add(f)
  })

  setItemRenderer(new FontItemRender)
  setDataRenderer(new FontButtonDataRenderer)

  def selectFamily(s: String): Unit = {
    val index = fonts.indexWhere(_.getFamily == s)
    setSelectedIndex(index)
  }
}


class FontItemRender extends BoxPane with ListView.ItemRenderer {
  protected val label: Label = new Label

  getStyles.put("horizontalAlignment", HorizontalAlignment.LEFT)
  getStyles.put("verticalAlignment", VerticalAlignment.CENTER)
  getStyles.put("padding", new Insets(2, 3, 2, 3))
  add(label)

  override def setSize(width: Int, height: Int): Unit = {
    super.setSize(width, height)
    validate()
  }

  def render(item: AnyRef, index: Int, listView: ListView, selected: Boolean, checked: Boolean, highlighted: Boolean, disabled: Boolean) {
    renderStyles(listView, selected, highlighted, disabled)

    if (item != null) {
      item match {
        case f: Font =>
          label.getStyles.put("font", f)
          StylesUtils.setFontSize(label, FontListButton.FONT_SIZE)
      }
      label.setText(toString(item))
    }

  }

  protected def renderStyles(listView: ListView, selected: Boolean, @SuppressWarnings(Array("unused")) highlighted: Boolean, disabled: Boolean) {
    var color: Color = null
    if (listView.isEnabled && !disabled) {
      if (selected) {
        if (listView.isFocused) {
          color = listView.getStyles.get("selectionColor").asInstanceOf[Color]
        }
        else {
          color = listView.getStyles.get("inactiveSelectionColor").asInstanceOf[Color]
        }
      }
      else {
        color = listView.getStyles.get("color").asInstanceOf[Color]
      }
    }
    else {
      color = listView.getStyles.get("disabledColor").asInstanceOf[Color]
    }
    label.getStyles.put("color", color)
  }

  def toString(item: AnyRef): String = {
    item match {
      case f: Font => f.getFontName
      case _ => "unknown"
    }
  }

  def getTextBounds: Bounds = {
    if (label.isVisible) label.getBounds else null
  }
}

class FontButtonDataRenderer extends ButtonDataRenderer {

  getStyles.put("horizontalAlignment", HorizontalAlignment.LEFT)

  override def render(data: AnyRef, button: Button, highlight: Boolean): Unit = {
    data match {
      case f: Font =>
        val dataMutable = new ButtonData(f.getFontName)
        button.getStyles.put("font", f)
        StylesUtils.setFontSize(button, FontListButton.FONT_SIZE)
        super.render(dataMutable, button, highlight)
      case _ => ()
    }
  }
}
