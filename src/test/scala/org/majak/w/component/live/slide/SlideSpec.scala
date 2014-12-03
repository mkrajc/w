package org.majak.w.component.live.slide

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers, PrivateMethodTester}

@RunWith(classOf[JUnitRunner])
class SlideSpec extends FlatSpec with Matchers {

  "Slide" should "compute font size relativly to slide size" in {
    new PrivateMethodTester {
      val slide = new Slide
      slide.setSize(0, 100)
      val computeFontSize = PrivateMethod[Int]('computeFontSize)
      slide.invokePrivate(computeFontSize())
      assert(10 === slide.fontSize)
    }
  }

  it should "partialy lift int array to array of accumulated sums" in {
    new PrivateMethodTester {
      val slide = new Slide
      val list = List(0,2,1,2,1)
      val toAccumulatedSum = PrivateMethod[List[Int]]('toAccumulatedSum)
      assert(List(0,2,3,5,6) === slide.invokePrivate(toAccumulatedSum.apply(list)))
    }
  }
}
