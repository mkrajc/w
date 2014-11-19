package org.majak.w.ui.event

import com.google.common.eventbus.{EventBus => GoogleEventBus}

trait Messaging {
	protected val eventBus = new GoogleEventBus
}