package org.majak.w.ui.pivot

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.{PushButton, Window}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers, PrivateMethodTester}

@RunWith(classOf[JUnitRunner])
class BindingSpec extends FlatSpec with Matchers {

  class BindingSpecClass extends Binding[Window] {
    @BXML
    var button: PushButton = _
  }

  "Bindings" should "return xml name from class" in {
    new PrivateMethodTester {
      val t = new BindingSpecClass
      val xmlNameValue = PrivateMethod[String]('xmlName)
      assert("bindingspecclass.xml" === t.invokePrivate(xmlNameValue()))
    }
  }

  it should "parse xml from class and return root" in {
    new PrivateMethodTester {
      val t = new BindingSpecClass
      val rootValue = PrivateMethod[Window]('root)
      val root = t invokePrivate rootValue()
      root shouldNot be(null);
      root shouldBe a[Window]
    }
  }

  it should "bind xml to instance and return root" in {
    val t = new BindingSpecClass
    t.bindUi
    t.button shouldNot be(null)
  }

}