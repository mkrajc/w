package org.majak.w.ui.event

import org.junit.runner.RunWith
import org.mockito.{Matchers, Mockito}
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

class Msg extends Messaging

class XEvent extends Event[XH] {
  def dispatch(h: XH) = {
    h.xh
  }
}

class XH extends Handler {
  def xh() = println("xh " + this.##)
}

@RunWith(classOf[JUnitRunner])
class EventBusSuite extends FlatSpec with MockitoSugar {

  import Mockito._

  "EventBus" should " notify event fire to each registered handler" in {
    val bus = new EventBus
    val event = mock[XEvent]
    bus.addHandler(event.getClass, new XH {})
    bus.fire(event)

    verify(event, times(1)).dispatch(Matchers.any())
  }

  it should " call handler method on fired event " in {
    val bus = new EventBus
    val event = new XEvent
    val h = mock[XH]
    bus.addHandler(event.getClass, h)
    bus.fire(event)

    verify(h, times(1)).xh()
  }

  it should " not call any handlers if removed all" in {
    val bus = new EventBus
    val event = new XEvent
    val h = mock[XH]
    bus.addHandler(event.getClass, h)
    bus.removeHandler(event.getClass, h)
    bus.fire(event)

    verifyZeroInteractions(h)
  }

}