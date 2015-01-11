package org.majak.w.component.image

import org.majak.w.controller.watchdog.image.{ThumbnailsSynchronizer, ImageDirectoryWatchDog}
import org.majak.w.ui.mvp.Presenter

class ImageLibraryPresenter(imgWatchDog: ImageDirectoryWatchDog) extends Presenter[ImageLibraryView] {

  override protected def onBind(v: ImageLibraryView): Unit = {
    //imgWatchDog.imagesLoaded.subscribe(images => images.foreach(v.addImage))
    // imgWatchDog.imageLoaded.subscribe(v.addImage(_))
    imgWatchDog.addImageSubject.subscribe(f => println(f.fileData.path))

    val thumbSync = new ThumbnailsSynchronizer(imgWatchDog)
    thumbSync.sync()

  }
}
