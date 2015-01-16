package org.majak.w.component.songlist

import org.apache.pivot.util.concurrent.{Task, TaskListener}
import org.apache.pivot.wtk.TaskAdapter
import org.majak.w.component.songlist.view.SongListView
import org.majak.w.model.song.data.SongModel.SongListItem
import org.majak.w.model.song.service.SongService
import org.majak.w.model.song.watchdog.SongSynchronizer
import org.majak.w.ui.component.pivot.searchbox.SearchHandler
import org.majak.w.ui.mvp.Presenter
import org.majak.w.utils.Utils

class SongListPresenter(val loadSongsTask: LoadSongsTask)
  extends Presenter[SongListView] {

  var data: List[SongListItem] = _

  override protected def onBind(v: SongListView) = {

    v.addSearchHandler(h = new SearchHandler {
      override def onSearch(text: String): Unit = {
        val filtered = data filter (s =>
          (Utils normalizeAccentedText s.name).toLowerCase contains Utils.normalizeAccentedText(text).toLowerCase)
        view showData filtered
      }

      override def onSearchCancel(): Unit = view.showData(data)
    })

    refresh()

  }

  def refresh(): Unit = {
    //view.setEnabled(false)
    val tl = new TaskListener[List[SongListItem]] {
      override def taskExecuted(task: Task[List[SongListItem]]): Unit = {
        data = task.getResult
        view.showData(data)
        //view.setEnabled(true)
      }

      override def executeFailed(task: Task[List[SongListItem]]): Unit = {
        logger.error("failed to load songs", task.getFault)
        //view.setEnabled(true)
      }
    }

    loadSongsTask.execute(new TaskAdapter[List[SongListItem]](tl))
  }
}

class LoadSongsTask(songSync: SongSynchronizer,
                    songService: SongService) extends Task[List[SongListItem]] {
  override def execute(): List[SongListItem] = {
    songSync.sync()
    songService.songs.map(s => SongListItem(s.id, s.name, s.name))
  }
}