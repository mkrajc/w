package org.majak.w.ui.wtk.utils


import java.util.{concurrent => juc}
import java.{lang => jl, util => ju}

import org.apache.pivot.collections.ArrayList

import scala.language.implicitConversions

object WtkConversions {

  def seqAsWtkList[A](seq: Seq[A]): org.apache.pivot.collections.List[A] = {
     (seq foldLeft new ArrayList[A])((list: ArrayList[A], item: A) => {list.add(item); list})
  }

}




