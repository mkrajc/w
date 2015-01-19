package org.majak.w.model.song.controller.task

import org.apache.pivot.util.concurrent.Task
import org.majak.w.model.song.data.SongModel.Song
import org.majak.w.model.song.service.SongService
import org.majak.w.model.song.watchdog.SongSynchronizer


class LoadSongsTask(songSync: SongSynchronizer,
                    songService: SongService) extends Task[List[Song]] {
  override def execute(): List[Song] = {
    songSync.sync()
    songService.songs
  }
}
