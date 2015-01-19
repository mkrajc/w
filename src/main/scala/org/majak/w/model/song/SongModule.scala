package org.majak.w.model.song

import org.majak.w.di.AppSettings
import org.majak.w.model.song.controller.SongController
import org.majak.w.model.song.controller.task.LoadSongsTask
import org.majak.w.model.song.parser.TxtSongParser
import org.majak.w.model.song.rx.SongEvent
import org.majak.w.model.song.service.{LuceneSongService, SongService}
import org.majak.w.model.song.watchdog.{SongDirectoryWatchDog, SongSynchronizer}
import org.majak.w.rx.ObservableCreator

trait SongModule extends AppSettings with ObservableCreator {
  private val songEvents = createEventSubject[SongEvent]

  val songController = new SongController(songEvents, songService, loadSongsTask)

  lazy val songService: SongService = new LuceneSongService
  lazy val songParsers = List(new TxtSongParser)

  lazy val songWatchDog = new SongDirectoryWatchDog(songsDir, songParsers)

  lazy val songSynchronizer = new SongSynchronizer(songWatchDog, songService, songParsers)

  lazy val loadSongsTask = new LoadSongsTask(songSynchronizer, songService)

}
