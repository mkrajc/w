package org.majak.w.ui.component.songlist

import org.apache.pivot.wtk.ListView
import org.apache.pivot.wtk.ListView.ItemRenderer
import org.majak.w.model.SongListItem
import org.majak.w.ui.component.common.wtk.WtkComponent
import org.majak.w.ui.component.songlist.view.SongListView
import org.majak.w.ui.mvp.Presenter
import org.majak.w.ui.wtk.utils.WtkConversions.seqAsWtkList

package view {

import org.majak.w.model.SongListItem
import org.majak.w.ui.component.common.Selectable
import org.majak.w.ui.component.common.view.ListView
import org.majak.w.ui.mvp.View

trait SongListView extends ListView[SongListItem] with Selectable[SongListItem] with View

}

class SongListPresenter extends Presenter[SongListView] {

  override protected def createViewImpl: SongListView = ???

}

class SongListComponent extends SongListView with WtkComponent[ListView] {

  override def onBind = {


  }

  override def showData(data: List[SongListItem]): Unit = root.setListData(seqAsWtkList(data))

  override def select(obj: SongListItem): Unit = root.setSelectedItem(obj)

  override def selected: Option[SongListItem] =
    if (root.getSelectedItem == null) None
    else Some(root.getSelectedItem.asInstanceOf[SongListItem])

}

