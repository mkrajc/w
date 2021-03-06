package org.mech.w;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.effects.FadeDecorator;
import org.apache.pivot.wtk.effects.Transition;
import org.apache.pivot.wtk.effects.TransitionListener;
import org.apache.pivot.wtk.effects.easing.Easing;
import org.apache.pivot.wtk.effects.easing.Quadratic;

public class CollapseTransition extends Transition {
    private Component component;
    private int initialWidth;
    private Easing easing = new Quadratic();
    private FadeDecorator fadeDecorator = new FadeDecorator();

    public CollapseTransition(Component component, int duration, int rate) {
	super(duration, rate, false);

	this.component = component;
	initialWidth = component.getWidth();
    }

    public Component getComponent() {
	return component;
    }

    @Override
    public void start(TransitionListener transitionListener) {
	component.getDecorators().add(fadeDecorator);

	super.start(transitionListener);
    }

    @Override
    public void stop() {
	System.out.println("removing decorator fadeDecorator");
	component.getDecorators().remove(fadeDecorator);

	super.stop();
    }

    @Override
    protected void update() {
	float percentComplete = getPercentComplete();

	if (percentComplete < 1.0f) {
	    int duration = getDuration();
	    int width = (int) (initialWidth * (1.0f - percentComplete));

	    width = (int) easing.easeInOut(getElapsedTime(), initialWidth, width - initialWidth, duration);

	    component.setPreferredWidth(width);

	    fadeDecorator.setOpacity(percentComplete);
	    component.repaint();
	}
    }
}
