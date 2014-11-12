package org.mech.w;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;

public class ListViewComponentTester extends ComponentTester {

    public ListViewComponentTester() {
	super("org/mech/w/test.xml");
    }
    
    @Override
    public void startup(Display display, Map<String, String> properties) throws Exception {
        super.startup(display, properties);
    }
    
    public static void main(String[] args) {
	DesktopApplicationContext.main(ListViewComponentTester.class, args);
    }

    @Override
    protected Object instance() {
	return new ListViews();
    }

}
