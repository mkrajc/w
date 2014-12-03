package org.majak.w.ui.component

import org.apache.pivot.collections.Map
import org.apache.pivot.wtk.TablePane.{Column, Row}
import org.apache.pivot.wtk._
import org.apache.pivot.wtk.media.Image
import org.majak.w.component.live.screen.LiveScreen
import org.majak.w.component.live.slide.{TextContent, ImageContent, Slide}
import org.majak.w.component.live.smallslide.LiveSmallSlide
import org.majak.w.component.main.menu.MainMenu
import org.majak.w.component.songlist.view.SongListViewHandler
import org.majak.w.di.UiModule
import org.majak.w.model.SongListItem
import org.majak.w.service.LocalSongService
import org.majak.w.ui.pivot.Conversions._

class ShowcaseApplication extends Application.Adapter with UiModule {
  override def songService = new LocalSongService

  private val NONE_LABEL = new Label("<NONE>")

  val pane = new TablePane

  val menuPanel = new BoxPane
  val compPanel = new FillPane
  val compButton = new ListButton


  val liveSmallSlide = new LiveSmallSlide
  liveSmallSlide.bindView

  private def createSlide(): Component = {
    val slide = new Slide(true)
    val slideRow = new Row()
    slideRow.setHeight("1*")
    val slidePane = new FillPane()
    slidePane.add(slide)
    slideRow.add(slidePane)

    val buttonRow = new Row(40)

    val buttonPanel = new BoxPane

    val clearTextButton = new PushButton("Clear Text")
    val clearImageButton = new PushButton("Clear Image")
    val slideButton1 = new PushButton("Show Text 1")
    val slideButton2 = new PushButton("Show Text 2")
    val imageButton = new PushButton("Show Image")

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
        slide.showContent(ImageContent(Image.load(getClass.getResource("/images/moon.jpg"))))
    })

    clearTextButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.clearTextContent()
    })

    clearImageButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button) =
        slide.clearImageContent()
    })

    buttonPanel.add(clearTextButton)
    buttonPanel.add(slideButton1)
    buttonPanel.add(slideButton2)
    buttonPanel.add(clearImageButton)
    buttonPanel.add(imageButton)

    buttonRow.add(buttonPanel)

    val slideTestPanel = new TablePane
    val c = new Column
    c.setWidth("1*")
    slideTestPanel.getColumns.add(c)
    slideTestPanel.getRows.add(buttonRow)
    slideTestPanel.getRows.add(slideRow)

    slideTestPanel
  }

  val components = scala.collection.immutable.Map[String, Component](

    "SongList" -> toPivotView(songListPresenter.view).asComponent,
    "MainMenu" -> (new MainMenu).asComponent,
    "LiveScreen" -> (new LiveScreen).asComponent,
    "LiveSmallSlide" -> liveSmallSlide.asComponent,
    "Slide" -> createSlide()

  )


  val r = new Row

  val r2 = new Row
  r2.setHeight("1*")

  val c = new Column
  c.setWidth("1*")

  (pane getColumns) add c
  (pane getRows) add r
  (pane getRows) add r2

  r2 add compPanel
  r add menuPanel

  compPanel.getStyles.put("padding", 10)

  menuPanel.getStyles.put("padding", 10)
  menuPanel.getStyles.put("verticalAlignment", VerticalAlignment.CENTER)
  menuPanel.add(compButton)

  songListPresenter.view.addHandler(new SongListViewHandler {
    override def onSongListItemSelected(item: Option[SongListItem]): Unit = {
      println(if (item.isDefined) item.get.name else "none")
    }
  })

  compButton setListData components.keys.toList
  (compButton getListButtonSelectionListeners) add new ListButtonSelectionListener.Adapter {
    override def selectedItemChanged(b: ListButton, psi: scala.Any): Unit = setupUI
  }

  setupUI

  private def setupUI: Unit = {
    val current: Option[Component] =
      if (compButton.getSelectedItem == null) None
      else components.get(compButton.getSelectedItem.asInstanceOf[String])

    compPanel removeAll()
    compPanel add current.getOrElse(NONE_LABEL)

  }

  override def startup(display: Display, properties: Map[String, String]): Unit = {
    val window = new Window

    window setContent pane
    window.setMaximized(true)

    songController.start

    window open display
  }
}

object ShowcaseApplication {
  def main(args: Array[String]) {
    DesktopApplicationContext.main(classOf[ShowcaseApplication], args)
  }
}