package org.majak.w.ui.pivot

import org.apache.pivot.collections.ArrayList
import org.apache.pivot.wtk.Component
import org.majak.w.ui.mvp.View

import scala.language.implicitConversions

/**
 * Contains implicit conversions between pivot collection types and scala collections
 */
object Conversions {
  /**
   * Returns pivot [[org.apache.pivot.collections.List]] from [[Seq]] instances
   *
   * @param seq to convert
   * @tparam A type of collection item
   * @return converted list
   */
  implicit def seqAsWtkList[A](seq: Seq[A]): org.apache.pivot.collections.List[A] = {
    (seq foldLeft new ArrayList[A])((list: ArrayList[A], item: A) => {
      list.add(item)
      list
    })
  }

  def toPivotView[V <: View](v: V): PivotView = v.asInstanceOf[PivotView]
  def toComponent[V <: View](v: V): Component = toPivotView(v).asComponent
}
