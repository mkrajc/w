package org.majak.w.component.songlist

import java.awt.Color

import org.apache.pivot.beans.BXML
import org.apache.pivot.json.JSON
import org.apache.pivot.wtk._
import org.apache.pivot.wtk.effects.ShadeDecorator
import org.majak.w.model.song.data.SongModel.SongListItem
import org.majak.w.ui.component.pivot.{SearchBox, SearchBoxEvent}
import org.majak.w.ui.component.{ERROR, LOADING, OK, State}
import org.majak.w.ui.pivot.Conversions._
import org.majak.w.ui.pivot.PivotComponent
import rx.lang.scala.Observable


class SongListComponent extends SongListView with PivotComponent {

  @BXML protected var listView: ListView = _
  @BXML protected var searchBox: SearchBox = _
  @BXML protected var songHeader: Label = _
  @BXML protected var refreshButton: PushButton = _

  private val loading = new ShadeDecorator(0.33f, Color.LIGHT_GRAY)


  override protected def onUiBind() = {
    listView.setItemRenderer(new SongListItemRenderer)
    listView.getListViewSelectionListeners.add(new ListViewSelectionListener.Adapter {
      override def selectedItemChanged(listView: ListView, previousSelectedItem: scala.Any): Unit = {
        fireSelected()
      }
    })

    refreshButton.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button): Unit = fireRefresh()
    })
  }

  override def setState(state: State): Unit = {
    def stopLoad(): Unit = {
      listView.getDecorators.remove(loading)
      listView.setEnabled(true)
      searchBox.setEnabled(true)
      refreshButton.setEnabled(true)
      songHeader.setText(header(0))
    }
    state match {
      case OK => stopLoad()
      case ERROR => stopLoad()
      case LOADING =>
        songHeader.setText(JSON.get(resources, "songList.header.loading"))
        listView.getDecorators.add(loading)
        listView.setEnabled(false)
        searchBox.setEnabled(false)
        refreshButton.setEnabled(false)
    }
  }

  override def showData(data: List[SongListItem]): Unit = {
    songHeader.setText(header(data.size))
    listView.clear()
    listView.setListData(data)
  }

  private def header(size: Int) = {
    JSON.get[String](resources, "songList.header.text").format(size)
  }

  override def select(obj: SongListItem): Unit = listView.setSelectedItem(obj)

  override def selected: Option[SongListItem] =
    if (listView.getSelectedItem == null) None
    else Some(listView.getSelectedItem.asInstanceOf[SongListItem])

  override def searchBoxObservable: Observable[SearchBoxEvent] = searchBox.observable

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

