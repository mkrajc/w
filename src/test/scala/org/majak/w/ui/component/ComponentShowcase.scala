package org.majak.w.ui.component

import org.apache.pivot.collections.Map
import org.apache.pivot.wtk.TablePane.{Column, Row}
import org.apache.pivot.wtk._
import org.majak.w.component.live.screen.LiveScreen
import org.majak.w.component.live.slide.{Slide, TextContent}
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


  val slide = new Slide
  //slide.addContent(ImageContent(Image.load(getClass.getResource("/images/cloud.jpg"))))
  slide.addContent(TextContent("hello"))


  val components = scala.collection.immutable.Map[String, Component](

    "SongList" -> toPivotView(songListPresenter.view).asComponent,
    "MainMenu" -> (new MainMenu).asComponent,
    "LiveScreen" -> (new LiveScreen).asComponent,
    "Slide" -> slide

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

  setupUI

  private def setupUI: Unit = {
    val current: Option[Component] =
      if (compButton.getSelectedItem == null) None
      else components.get(compButton.getSelectedItem.asInstanceOf[String])

    compPanel removeAll()
    compPanel add current.getOrElse(NONE_LABEL)

  }

  compButton setListData components.keys.toList
  (compButton getListButtonSelectionListeners) add new ListButtonSelectionListener.Adapter {
    override def selectedItemChanged(b: ListButton, psi: scala.Any): Unit = setupUI
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