package org.majak.w.ui.component

import org.apache.pivot.wtk._
import org.apache.pivot.wtk.TablePane.{Column, Row}
import org.apache.pivot.wtk.media.Image
import org.majak.w.component.live.slide._
import org.majak.w.component.slide.{TextContent, ImageContent, EmptyFront, Slide}

object SlideComponentProvider {
  def createSlideTestComponent: Component = {
    val slide = new Slide(true)
    val slideRow = new Row()
    slideRow.setHeight("1*")
    val slidePane = new FillPane()
    slidePane.add(slide)
    slideRow.add(slidePane)

    val buttonRow = new Row(50)

    val buttonPanel = new FlowPane

    val clearTextButton = new PushButton("Clear Text")
    val clearImageButton = new PushButton("Clear Image")
    val slideButton1 = new PushButton("Show Text 1")
    val slideButton2 = new PushButton("Show Text 2")
    val imageButton = new PushButton("Show Image")
    val left = new PushButton("L")
    val center = new PushButton("C")
    val right = new PushButton("R")

    val top = new PushButton("T")
    val middle = new PushButton("M")
    val bottom = new PushButton("B")

    slideButton1.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.showContent(TextContent(List("text 1", "new line", "another day without linebreak", "new line")))
    })

    slideButton2.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.showContent(TextContent(List("text 2", "text 2", "very long long logn long long long line")))
    })

    imageButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.showContent(ImageContent(Image.loadFromCache(getClass.getResource("/images/moon.jpg"))))
    })

    clearTextButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.showContent(EmptyFront)
    })

    clearImageButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.showContent(EmptyFront)
    })

    left.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.setHorizontalAlign(HorizontalAlignment.LEFT)
    })

    right.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.setHorizontalAlign(HorizontalAlignment.RIGHT)
    })

    center.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.setHorizontalAlign(HorizontalAlignment.CENTER)
    })

    top.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.setVerticalAlign(VerticalAlignment.TOP)
    })

    middle.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.setVerticalAlign(VerticalAlignment.CENTER)
    })

    bottom.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.setVerticalAlign(VerticalAlignment.BOTTOM)
    })

    buttonPanel.add(clearTextButton)
    buttonPanel.add(slideButton1)
    buttonPanel.add(slideButton2)
    buttonPanel.add(clearImageButton)
    buttonPanel.add(imageButton)

    buttonPanel.add(left)
    buttonPanel.add(center)
    buttonPanel.add(right)

    buttonPanel.add(top)
    buttonPanel.add(middle)
    buttonPanel.add(bottom)

    buttonRow.add(buttonPanel)

    val slideTestPanel = new TablePane
    val c = new Column
    c.setWidth("1*")
    slideTestPanel.getColumns.add(c)
    slideTestPanel.getRows.add(buttonRow)
    slideTestPanel.getRows.add(slideRow)

    slideTestPanel
  }
}
