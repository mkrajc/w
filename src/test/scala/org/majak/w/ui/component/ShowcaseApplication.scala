package org.majak.w.ui.component

import org.apache.pivot.collections
import org.apache.pivot.wtk.TablePane.{Column, Row}
import org.apache.pivot.wtk._
import org.majak.w.component.live.screen.LiveScreen
import org.majak.w.component.main.menu.MainMenu
import org.majak.w.component.smallslide.LiveSmallSlide
import org.majak.w.component.songlist.{SongItemSelected, SongListUiEvent}
import org.majak.w.di.AppModule
import org.majak.w.service.LocalSongService
import org.majak.w.ui.pivot.Conversions._

class ShowcaseApplication extends Application.Adapter with AppModule {
  override lazy val songService = new LocalSongService

  private val NONE_LABEL = new Label("<NONE>")

  val pane = new TablePane

  val menuPanel = new BoxPane
  val compPanel = new FillPane
  val compButton = new ListButton


  val liveSmallSlide = new LiveSmallSlide
  liveSmallSlide.bindView

  val components = scala.collection.immutable.Map[String, Component](

    "SongList" -> toPivotView(songListPresenter.view).asComponent,
    "MainMenu" -> (new MainMenu).asComponent,
    "LiveScreen" -> (new LiveScreen).asComponent,
    "LiveSmallSlide" -> liveSmallSlide.asComponent,
    "PreviewSmallSlide" -> toPivotView(previewSmallSlidePresenter.view).asComponent,
    "Slide" -> SlideComponentProvider.createSlideTestComponent,
    "SongDetail" -> SongDetailProvider.createSongDetailTest,
    "ImageLibrary" -> toPivotView(imageLibraryPresenter.view).asComponent

  )

  val r = new Row

  val r2 = new Row
  r2.setHeight("1*")

  val c = new Column
  c.setWidth("1*")

  pane.getColumns.add(c)
  pane.getRows.add(r)
  pane.getRows.add(r2)

  r2 add compPanel
  r add menuPanel

  compPanel.getStyles.put("padding", 10)

  menuPanel.getStyles.put("padding", 10)
  menuPanel.getStyles.put("verticalAlignment", VerticalAlignment.CENTER)
  menuPanel.add(compButton)

  songListPresenter.view.observable.subscribe(onNext = e => handleSongListEvent(e))

  private def handleSongListEvent(event: SongListUiEvent): Unit = {
    event match {
      case SongItemSelected(s) => println(if (s.isDefined) s.get.name else "none")
    }
  }

  compButton setListData components.keys.toList
  compButton.getListButtonSelectionListeners.add(new ListButtonSelectionListener.Adapter {
    override def selectedItemChanged(b: ListButton, psi: scala.Any): Unit = setupUI()
  })

  setupUI()

  private def setupUI(): Unit = {
    val current: Option[Component] =
      if (compButton.getSelectedItem == null) None
      else components.get(compButton.getSelectedItem.asInstanceOf[String])

    compPanel removeAll()
    compPanel add current.getOrElse(NONE_LABEL)

  }

  override def startup(display: Display, properties: collections.Map[String, String]): Unit = {
    val window = new Window

    window setContent pane
    window.setMaximized(true)

    window open display
  }
}

object ShowcaseApplication {
  def main(args: Array[String]) {
    DesktopApplicationContext.main(classOf[ShowcaseApplication], args)
  }
}