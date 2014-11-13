package org.majak.w.ui.event

class Msg extends Messaging

class XEvent extends Event[XH] {
  def dispatch(h: XH) = {
    h.xh
  }
}

class XH extends Handler {
  def xh() = println("xh " + this.##)
}

object EventBusSuite {
  def main(args: Array[String]) {
    val e = new XEvent

    val h = new XH
    val h2 = new XH

    val b = new EventBus
    b.addHandler(classOf[XEvent], h)
    b.addHandler(classOf[XEvent], h2)
    b.fire(e)
    b.removeHandler(classOf[XEvent], h)
    b.fire(e)
  }
}