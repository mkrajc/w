package org.majak.w.component.live.slide

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FlatSpec, Matchers, PrivateMethodTester}
import org.mockito.Mockito._
import org.mockito.Matchers._

@RunWith(classOf[JUnitRunner])
class SlideSpec extends FlatSpec with Matchers with MockitoSugar {

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
      val list = List(0, 2, 1, 2, 1)
      val toAccumulatedSum = PrivateMethod[List[Int]]('toAccumulatedSum)
      assert(List(0, 2, 3, 5, 6) === slide.invokePrivate(toAccumulatedSum.apply(list)))
    }
  }

  it should "notify listener when content changed" in {
    val slide = new Slide
    val m = mock[SlideListener]
    slide.addSlideListener(m)
    slide.showContent(TextContent(Nil))
    slide.showContent(ClearTextContent())
    verify(m, times(2)).onContent(any(classOf[Content]))
  }
}
