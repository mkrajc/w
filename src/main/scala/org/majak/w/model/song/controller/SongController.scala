package org.majak.w.model.song.controller

import org.apache.pivot.util.concurrent.{Task, TaskListener}
import org.apache.pivot.wtk.TaskAdapter
import org.majak.w.component.songlist.{Refresh, SongItemSelected, SongListUiEvent}
import org.majak.w.model.song.controller.task.LoadSongsTask
import org.majak.w.model.song.data.SongModel.Song
import org.majak.w.model.song.rx.{SongsRefreshed, SongSelected, SongEvent}
import org.majak.w.model.song.service.SongService
import org.majak.w.rx.{Action, ObservableObject}
import org.slf4j.LoggerFactory
import rx.lang.scala.{Observable, Subject}


class SongController(events: Subject[SongEvent],
                     songService: SongService,
                     loadSongsTask: LoadSongsTask
                      ) extends ObservableObject {

  val logger = LoggerFactory.getLogger(getClass)

  def subscribeSongListEvents(songListEvents: Observable[SongListUiEvent]): Unit = {
    songListEvents.subscribe(e => e match {
      case Refresh(action) => refreshSongs(action)
      case SongItemSelected(s) =>
        if (s.isDefined) {
          val songOption = songService.findById(s.get.id)
          songOption.map(song => events.onNext(SongSelected(song)))
        }
    })
  }

  override def observable: Observable[SongEvent] = events

  def refreshSongs(action: Action): Unit = {
    action.onStart()

    logger.info("songs loading ...")
    val tl = new TaskListener[List[Song]] {
      override def taskExecuted(task: Task[List[Song]]): Unit = {
        logger.info("songs loaded successfully")
        action.onEnd()
        events.onNext(SongsRefreshed(task.getResult))
      }

      override def executeFailed(task: Task[List[Song]]): Unit = {
        logger.error("failed to load songs", task.getFault)
        action.onFailure(task.getFault)
      }
    }

    loadSongsTask.execute(new TaskAdapter[List[Song]](tl))
  }
}
