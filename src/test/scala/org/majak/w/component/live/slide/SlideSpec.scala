package org.majak.w.component.live.slide

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers, PrivateMethodTester}

@RunWith(classOf[JUnitRunner])
class SlideSpec extends FlatSpec with Matchers with MockitoSugar {

  "Slide" should "compute font size relatively to slide size" in {
    new PrivateMethodTester {
      val slide = new Slide
      slide.setSize(0, 100)
      val computeFontSize = PrivateMethod[Int]('computeFontSize)
      slide.invokePrivate(computeFontSize())
      assert(10 === slide.fontSize)
    }
  }

  it should "partially lift int array to array of accumulated sums" in {
    new PrivateMethodTester {
      val slide = new Slide
      val list = List(0, 2, 1, 2, 1)
      val toAccumulatedSum = PrivateMethod[List[Int]]('toAccumulatedSum)
      assert(List(0, 2, 3, 5, 6) === slide.invokePrivate(toAccumulatedSum.apply(list)))
    }
  }

  it should "notify listener when content changed" in {
    val slide = new Slide
    var slideContentChangedCalled = 0
    slide.addSlideContentListener(_ => slideContentChangedCalled = slideContentChangedCalled + 1)
    slide.showContent(TextContent(Nil))
    slide.showContent(ClearTextContent())
    slideContentChangedCalled shouldEqual 2
  }
}
