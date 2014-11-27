package test;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Label;

/**
 * Created by Martin on 26.11.2014.
 */
public class TLabel extends Label {
    public TLabel(String text) {
        super(text);
    }

    @Override
    protected void installSkin(Class<? extends Component> componentClass) {
        setSkin(new TestLabelSkin());
    }
}
