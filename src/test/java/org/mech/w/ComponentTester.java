package org.mech.w;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

public abstract class ComponentTester extends Application.Adapter {

    private String xml;

    public ComponentTester(final String xml) {
	this.xml = xml;
    }

    protected abstract Object instance();

    @Override
    public void startup(Display display, Map<String, String> properties) throws Exception {
	final Window w = new Window();
	final BXMLSerializer bxmlSerializer = new BXMLSerializer();
	final Component content = (Component) bxmlSerializer.readObject(getClass().getClassLoader().getResource(xml), null);
	final Object val = instance();
	if (val != null) {
	    bxmlSerializer.bind(val);
	}
	w.setContent(content);
	w.open(display);
    }

}