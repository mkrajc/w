package org.majak.w.ui.pivot



import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.{Component, PushButton, Window}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers, PrivateMethodTester}

@RunWith(classOf[JUnitRunner])
class BindingSpec extends FlatSpec with Matchers {

  class BindingSpecClass extends Binding {
    @BXML
    var button: PushButton = _
  }

  class BindingWithoutXmlSpecClass extends Binding {}

  "Bindings" should "return xml name from class" in {
    new PrivateMethodTester {
      val t = new BindingSpecClass
      val xmlNameValue = PrivateMethod[String]('xmlName)
      assert("bindingspecclass.xml" === t.invokePrivate(xmlNameValue()))
    }
  }

  it should "parse xml from class and return root component if xml exists" in {
    new PrivateMethodTester {
      val t = new BindingSpecClass
      val rootValue = PrivateMethod[Option[Component]]('readXml)
      val root = t invokePrivate rootValue()
      root shouldNot be(null)
      root shouldBe a[Option[_]]
      root.get shouldBe a[Window]
    }
  }

  it should "skip parsing xml from class and return root component as None if xml does not exists" in {
    new PrivateMethodTester {
      val t = new BindingWithoutXmlSpecClass
      val rootValue = PrivateMethod[Option[Component]]('readXml)
      val root = t invokePrivate rootValue()
      root shouldNot be(null)
      root should be(scala.None)
    }
  }

  it should "bind xml to instance and return root" in {
    val t = new BindingSpecClass
    t.bindUi
    t.button shouldNot be(null)
  }

}