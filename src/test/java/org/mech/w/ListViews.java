package org.mech.w;

import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtk.content.ListItem;

import java.net.URL;

public class ListViews extends Window implements Bindable {
    private Label selectionLabel = null;
    private ListView listView = null;

    @Override
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {
	selectionLabel = (Label) namespace.get("selectionLabel");
	listView = (ListView) namespace.get("listView");

	listView.getListViewSelectionListeners().add(new ListViewSelectionListener() {
	    @Override
	    public void selectedRangeAdded(ListView listView, int rangeStart, int rangeEnd) {
		updateSelection(listView);
	    }

	    @Override
	    public void selectedRangeRemoved(ListView listView, int rangeStart, int rangeEnd) {
		updateSelection(listView);
	    }

	    @Override
	    public void selectedRangesChanged(ListView listView, Sequence<Span> previousSelectedRanges) {
		if (previousSelectedRanges != null && previousSelectedRanges != listView.getSelectedRanges()) {
		    updateSelection(listView);
		}
	    }

	    @Override
	    public void selectedItemChanged(ListView listView, Object previousSelectedItem) {
		// No-op
	    }

	    private void updateSelection(ListView listView) {
		String selectionText = "";

		Sequence<Span> selectedRanges = listView.getSelectedRanges();
		for (int i = 0, n = selectedRanges.getLength(); i < n; i++) {
		    Span selectedRange = selectedRanges.get(i);

		    for (int j = selectedRange.start; j <= selectedRange.end; j++) {
			if (selectionText.length() > 0) {
			    selectionText += ", ";
			}

			Object item = listView.getListData().get(j);
			String text;
			if (item instanceof ListItem) { // item is a listItem
							// (for example because
							// it has an image)
			    text = ((ListItem) item).getText();
			} else { // item is a standard item for listData
			    text = item.toString();
			}
			selectionText += text;
		    }
		}

		selectionLabel.setText(selectionText);
	    }
	});
    }
    
}
