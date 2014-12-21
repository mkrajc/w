package org.majak.w.component.image

import org.majak.w.controller.ImageDirectoryWatchDog
import org.majak.w.ui.mvp.Presenter

class ImageLibraryPresenter(imgWatchDog: ImageDirectoryWatchDog) extends Presenter[ImageLibraryView] {

  override protected def onBind(v: ImageLibraryView): Unit = {
    imgWatchDog.observable.subscribe(img => v.addImage(img))

    imgWatchDog.scan()
  }
}
