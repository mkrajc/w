package org.majak.w.ui.component

import org.apache.pivot.collections.Map
import org.apache.pivot.wtk.Button.State
import org.apache.pivot.wtk.TablePane.{Column, Row}
import org.apache.pivot.wtk._
import org.majak.w.di.PresenterModule
import org.majak.w.ui.component.common.wtk.WtkView
import org.majak.w.ui.component.songlist.SongListComponent
import org.majak.w.ui.wtk.utils.WtkConversions

class ShowcaseApplication extends Application.Adapter with PresenterModule {
  val pane = new TablePane

  val wrapper = new FlowPane

  val menuPanel = new BoxPane
  val compPanel = new FillPane


  val compButton = new ListButton
  val wrapCheck = new Checkbox("wrap")

  val components = List(
    (songListPresenter.view.asInstanceOf[WtkView]).asComponent,
    new Checkbox("checkbox")
  )

  setupLayout

  private def setupLayout = {
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
  }


  private def setupUI: Unit ={
    val current: Option[Component] =
      if (compButton.getSelectedItem == null) None
      else Some(compButton.getSelectedItem.asInstanceOf[Component])

    compPanel removeAll()
    wrapper removeAll()

    if (wrapCheck.getState == Button.State.SELECTED) {
      wrapper add current.getOrElse(new Label("<none>"))
      compPanel add wrapper
    } else {
      compPanel add current.getOrElse(new Label("<none>"))
    }
  }

  compButton setListData WtkConversions.seqAsWtkList(components)
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

    window open display
  }
}

object ShowcaseApplication {
  def main(args: Array[String]) {
    DesktopApplicationContext.main(classOf[ShowcaseApplication], args)
  }
}
