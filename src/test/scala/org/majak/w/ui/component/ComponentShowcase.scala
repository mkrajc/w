package org.majak.w.ui.component

import org.apache.pivot.collections.Map
import org.apache.pivot.wtk.Button.State
import org.apache.pivot.wtk.TablePane.{Column, Row}
import org.apache.pivot.wtk._
import org.majak.w.component.songlist.view.SongListViewHandler
import org.majak.w.di.Module
import org.majak.w.model.SongListItem
import org.majak.w.service.LocalSongService
import org.majak.w.ui.pivot.PivotView
import org.majak.w.ui.pivot.Conversions._

class ShowcaseApplication extends Application.Adapter with Module {
  override def songService = new LocalSongService

  private val NONE_LABEL = new Label("<NONE>")

  val pane = new TablePane

  val wrapper = new FlowPane

  val menuPanel = new BoxPane
  val compPanel = new FillPane

  val compButton = new ListButton
  val wrapCheck = new Checkbox("wrap")

  val components = scala.collection.immutable.Map[String, Component](

    "SongList" -> (songListPresenter.view.asInstanceOf[PivotView]).asComponent,
    "Checkbox" -> new Checkbox("check")

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
  menuPanel.add(wrapCheck)

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
    wrapper removeAll()

    if (wrapCheck.getState == Button.State.SELECTED) {
      wrapper add current.getOrElse(NONE_LABEL)
      compPanel add wrapper
    } else {
      compPanel add current.getOrElse(NONE_LABEL)
    }
  }

  compButton setListData components.keys.toList
  (compButton getListButtonSelectionListeners) add new ListButtonSelectionListener {
    override def selectedIndexChanged(b: ListButton, psi: Int): Unit = {}

    override def selectedItemChanged(b: ListButton, psi: scala.Any): Unit = setupUI
  }

  (wrapCheck getButtonStateListeners) add new ButtonStateListener {
    override def stateChanged(b: Button, ps: State) = setupUI
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