package org.majak.w.ui.component.songlist

import org.apache.pivot.beans.BXML
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk._
import org.majak.w.controller.{SongController, SongIndexChangeListener}
import org.majak.w.model.SongListItem
import org.majak.w.service.SongService
import org.majak.w.ui.component.common.wtk.WtkComponent
import org.majak.w.ui.component.songlist.view.{SongListView, SongListViewHandler}
import org.majak.w.ui.mvp.{Presenter, View}
import org.majak.w.ui.wtk.utils.WtkConversions.seqAsWtkList
import org.majak.w.utils.Utils

import scala.collection.mutable.ListBuffer

package view {

import org.majak.w.model.SongListItem
import org.majak.w.ui.component.common.Selectable
import org.majak.w.ui.component.common.view.ListView
import org.majak.w.ui.mvp.View

trait SongListView extends ListView[SongListItem] with Selectable[SongListItem] with View {
  def addHandler(h: SongListViewHandler)

  def removeHandler(h: SongListViewHandler)
}

trait SongListViewHandler {
  def onSongListItemSelected(item: Option[SongListItem])
}

}

class SongListPresenter(songController: SongController,
                        songService: SongService)
  extends Presenter[SongListView] with SongIndexChangeListener {

  override protected def createViewImpl: SongListView = new SongListComponent

  override protected def onBind(v: View) = songController += this

  override def onSongIndexChanged: Unit = view.showData(songService.getAllSongs map (s => SongListItem(s.id, s.name, s.name)))
}

class SongListComponent extends SongListView with WtkComponent[ScrollPane] {

  val viewHandlers = new ListBuffer[SongListViewHandler]

  bindUi

  @BXML
  private var listView: ListView = _

  override def addHandler(h: SongListViewHandler): Unit = viewHandlers += h

  override def removeHandler(h: SongListViewHandler): Unit = viewHandlers -= h

  override protected def onUiBind = {
    listView.setItemRenderer(new SongListItemRenderer)
    listView.getListViewSelectionListeners.add(new ListViewSelectionListener {
      override def selectedRangeRemoved(listView: ListView, rangeStart: Int, rangeEnd: Int): Unit = {}
      override def selectedRangesChanged(listView: ListView, previousSelectedRanges: Sequence[Span]): Unit = {}
      override def selectedRangeAdded(listView: ListView, rangeStart: Int, rangeEnd: Int): Unit = {}
      override def selectedItemChanged(listView: ListView, previousSelectedItem: scala.Any): Unit = {
        val selected = Utils.nullAsOption[SongListItem](listView.getSelectedItem)
        viewHandlers foreach (_.onSongListItemSelected(selected))
      }
    })
  }

  override def showData(data: List[SongListItem]): Unit = listView.setListData(seqAsWtkList(data))

  override def select(obj: SongListItem): Unit = listView.setSelectedItem(obj)

  override def selected: Option[SongListItem] =
    if (listView.getSelectedItem == null) None
    else Some(listView.getSelectedItem.asInstanceOf[SongListItem])

}

class SongListItemRenderer extends BoxPane with ListView.ItemRenderer {
  protected val label: Label = new Label
  getStyles.put("horizontalAlignment", HorizontalAlignment.LEFT)
  getStyles.put("verticalAlignment", VerticalAlignment.CENTER)
  getStyles.put("padding", new Insets(7, 4, 7, 4))
  add(label)

  override def render(item: scala.Any, index: Int, listView: ListView, selected: Boolean, checked: Boolean, highlighted: Boolean, disabled: Boolean): Unit = {
    if (item != null) {
      val songItem = item.asInstanceOf[SongListItem]
      label.setText(songItem.name)
      label.setTooltipText(songItem.hint)
      label.setTooltipDelay(100)
    }
  }

  override def toString(item: scala.Any): String = item match {
    case i: SongListItem => i.name
    case _ => null
  }

  override def setSize(width: Int, height: Int) {
    super.setSize(width, height)
    validate
  }
}

