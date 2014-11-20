package org.majak.w.ui.pivot

import org.apache.pivot.collections.ArrayList

object Conversions {
  implicit def seqAsWtkList[A](seq: Seq[A]): org.apache.pivot.collections.List[A] = {
    (seq foldLeft new ArrayList[A])((list: ArrayList[A], item: A) => {list.add(item); list})
  }
}
