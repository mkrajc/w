package org.majak.w.ui

import org.apache.pivot.wtk.Label
import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner
import org.apache.pivot.wtk.Window
import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.PushButton

@RunWith(classOf[JUnitRunner])
class BindingSpec extends FlatSpec with Matchers {

  class BindingSpecClass extends Binding {
    @BXML
    var button: PushButton = _
  }

  "Bindings" should "return xml name from class" in {
    val t = new BindingSpecClass
    assert("testingclass.xml" === t.xmlName)
  }

  it should "parse xml from class and return root" in {
    val t = new BindingSpecClass
    val root = t.parse(classOf[Window])
    root shouldNot be(null);
    root shouldBe a[Window]
  }

  it should "bind xml to instance and return root" in {
    val t = new BindingSpecClass
    val root = t.bind(classOf[Window])
    root shouldNot be (null);
    root shouldBe a[Window]
    t.button shouldNot be (null)
  }

}