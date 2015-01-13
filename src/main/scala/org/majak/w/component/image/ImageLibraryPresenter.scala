package org.majak.w.component.image

import org.apache.pivot.util.concurrent.{Task, TaskListener}
import org.apache.pivot.wtk.TaskAdapter
import org.majak.w.controller.ControllerSettings
import org.majak.w.model.image.data.Thumbnail
import org.majak.w.model.image.watchdog.{ImageDirectoryWatchDog, ThumbnailsSynchronizer}
import org.majak.w.ui.mvp.Presenter
import rx.lang.scala.Observable

class ImageLibraryPresenter(imgWatchDog: ImageDirectoryWatchDog) extends Presenter[ImageLibraryView] with ControllerSettings {

  val thumbSync = new ThumbnailsSynchronizer(imgWatchDog)

  var imagesObservable: Observable[ImageLibraryUiEvent] = _

  override protected def onBind(v: ImageLibraryView): Unit = {
    view.setThumbSize(THUMBS_SIZE)
    view.observable.subscribe(onNext = e => handleEvent(e))
    this.imagesObservable = view.observable.filter(_.isInstanceOf[ThumbnailClicked])

    refresh()
  }

  private def handleEvent(event: ImageLibraryUiEvent): Unit = {
    event match {
      case Refresh => refresh()
      case ThumbnailClicked(_) => ()
    }

  }

  def refresh(): Unit = {
    view.setEnabled(false)
    val tl = new TaskListener[Set[Thumbnail]] {
      override def taskExecuted(task: Task[Set[Thumbnail]]): Unit = {
        view.showThumbnails(task.getResult)
        view.setEnabled(true)
      }

      override def executeFailed(task: Task[Set[Thumbnail]]): Unit = {
        logger.error("failed to load thumbnails", task.getFault)
        view.setEnabled(true)
      }
    }

    val task = new LoadThumbnailsTask(thumbSync)
    task.execute(new TaskAdapter[Set[Thumbnail]](tl))
  }

}

class LoadThumbnailsTask(val thumbnailsSynchronizer: ThumbnailsSynchronizer) extends Task[Set[Thumbnail]] {
  override def execute(): Set[Thumbnail] = {
    thumbnailsSynchronizer.sync()
    thumbnailsSynchronizer.thumbs
  }
}
