package org.majak.w.component.songlist.pivot

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.songlist.view.{SongListView, SongListViewHandler}
import org.majak.w.model.song.data.SongModel
import org.majak.w.model.song.data.SongModel.SongListItem
import org.majak.w.ui.component.pivot.searchbox.{SearchBox, SearchHandler}
import org.majak.w.ui.pivot.Conversions._
import org.majak.w.ui.pivot.PivotComponent
import org.majak.w.utils.Utils

import scala.collection.mutable.ListBuffer

class SongListComponent extends SongListView with PivotComponent {

  val viewHandlers = new ListBuffer[SongListViewHandler]

  @BXML
  protected var listView: ListView = _

  @BXML
  protected var searchBox: SearchBox = _

  override def addHandler(h: SongListViewHandler): Unit = viewHandlers += h

  override def removeHandler(h: SongListViewHandler): Unit = viewHandlers -= h

  override protected def onUiBind = {
    listView.setItemRenderer(new SongListItemRenderer)
    listView.getListViewSelectionListeners.add(new ListViewSelectionListener.Adapter {
      override def selectedItemChanged(listView: ListView, previousSelectedItem: scala.Any): Unit = {
        val selected = Utils.nullAsOption[SongListItem](listView.getSelectedItem)
        viewHandlers foreach (_.onSongListItemSelected(selected))
      }
    })
  }

  override def showData(data: List[SongListItem]): Unit = listView.setListData(data)

  override def select(obj: SongListItem): Unit = listView.setSelectedItem(obj)

  override def selected: Option[SongListItem] =
    if (listView.getSelectedItem == null) None
    else Some(listView.getSelectedItem.asInstanceOf[SongListItem])

  override def addSearchHandler(h: SearchHandler): Unit = searchBox addSearchHandler h

  override def removeSearchHandler(h: SearchHandler): Unit = searchBox removeSearchHandler h
}

private class SongListItemRenderer extends BoxPane with ListView.ItemRenderer {
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
    validate()
  }
}

