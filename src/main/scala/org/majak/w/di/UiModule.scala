package org.majak.w.di

import org.majak.w.ui.mvp.{Presenter, View}


trait UiModule extends Module {

  protected def doBind[V <: View, P <: Presenter[V]](p: P, view: V): P = {
    if (view == null) throw new IllegalArgumentException("cannot bind presenter with undefined view")
    else p bind view
    p
  }


}
