package org.majak.w.ui.mvp

import org.scalatest.{Matchers, FlatSpec}

class PresenterSpec extends FlatSpec with Matchers {

  class TView extends View {
    override def unbindView(): Unit = ()

    override def bindView(): Unit = ()
  }

  "Presenter" should "initialize without view" in {
    val p = new Presenter[View] {}
    p shouldNot be(null)
  }

  it should "provide bind state information" in {
    val p = new Presenter[TView] {}
    p.bound shouldBe false
    p.bind(new TView)
    p.bound shouldBe true
  }

  it should "call bind view, and onBind during bind" in {
    var bindViewCalled = false
    var onBindCalled = false
    val p = new Presenter[TView] {
      override protected def onBind(v: TView): Unit = onBindCalled = true
    }
    p.bind(new TView {
      override def bindView(): Unit = bindViewCalled = true
    })
    bindViewCalled shouldBe true
    onBindCalled shouldBe true
  }

  it should "call unbind view, and onUnbind during unbind" in {
    var unbindViewCalled = false
    var onUnbindCalled = false

    val p = new Presenter[TView] {
      override protected def onUnbind(v: TView): Unit = onUnbindCalled = true
    }
    p.bind(new TView {
      override def unbindView(): Unit = unbindViewCalled = true
    })
    p.unbind()

    unbindViewCalled shouldBe true
    onUnbindCalled shouldBe true
  }

  it should "failed on calling view before binding" in {
    intercept[IllegalStateException] {
      val p = new Presenter[TView] {}
      p.view
    }
  }

  it should "return same view, after binding" in {
    val p = new Presenter[TView] {}
    val v = new TView
    p bind v
    p.view shouldEqual v
  }

  it should "failed on calling view after unbinding" in {
    intercept[IllegalStateException] {
      val p = new Presenter[TView] {}
      val v = new TView
      p bind v
      p unbind()
      p.view
    }
  }

  it should "notify bind listeners on bind when exists" in {
    var bindListenerNotified = false
    val p = new Presenter[TView] {}
    val v = new TView
    val listener = (v: View) => bindListenerNotified = true
    p.addBindListener(listener)
    p bind v

    bindListenerNotified shouldBe true

  }

  it should "not notify remove bind listeners" in {
    var bindListenerNotified = false
    val p = new Presenter[TView] {}
    val v = new TView
    val listener = (v: View) => bindListenerNotified = true
    p.addBindListener(listener)
    p.removeBindListener(listener)
    p bind v

    bindListenerNotified shouldBe false

  }

}
