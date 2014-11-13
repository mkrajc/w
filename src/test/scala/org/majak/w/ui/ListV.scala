package org.majak.w.ui

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.Border
import org.apache.pivot.wtk.Label
import org.apache.pivot.wtk.ListView
import org.apache.pivot.wtk.ListViewSelectionListener
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.Span
import org.apache.pivot.wtk.content.ListItem
import org.majak.w.model.SongListItem
import org.majak.w.ui.component.songlist.view.SongListView

class ListV extends Binding[Border] with SongListView {

  @BXML
  var selectionLabel: Label = _

  @BXML
  var listView: ListView = _
  
  def r = root
  
  override def xmlName = "/org/mech/w/test.xml"
  
  override def onBind = {

    listView.getListViewSelectionListeners().add(new ListViewSelectionListener() {
      def selectedRangeAdded(listView: ListView, rangeStart: Int, rangeEnd: Int) {
        updateSelection(listView);
      }

      def selectedRangeRemoved(listView: ListView, rangeStart: Int, rangeEnd: Int) {
        updateSelection(listView);
      }

      def selectedRangesChanged(listView: ListView, previousSelectedRanges: Sequence[Span]) {
        if (previousSelectedRanges != null && previousSelectedRanges != listView.getSelectedRanges()) {
          updateSelection(listView);
        }
      }

      def selectedItemChanged(listView: ListView, previousSelectedItem: Object) {
      }

      private def updateSelection(listView: ListView) {
        var selectionText = "";
        var selectedRanges: Sequence[Span] = listView.getSelectedRanges();

        for (i <- 0 until selectedRanges.getLength()) {
          val selectedRange = selectedRanges.get(i);

          for (j <- selectedRange.start to selectedRange.end) {
            if (selectionText.length() > 0) {
              selectionText += ", ";
            }

            val item = listView.getListData().get(j);
            val text = item match {
              case t: ListItem => t.getText()
              case x           => x.toString
            }
            selectionText += text;
          }
        }

        selectionLabel.setText(selectionText);
      }
    })
  }

  override def showData(data: List[SongListItem]): Unit = ???

  override def select(obj: SongListItem): Unit = ???

  override def selected: Option[SongListItem] = ???
}