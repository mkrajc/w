package org.majak.w.model.song

import org.majak.w.component.songlist.LoadSongsTask
import org.majak.w.di.AppSettings
import org.majak.w.model.song.parser.TxtSongParser
import org.majak.w.model.song.service.SongService
import org.majak.w.model.song.watchdog.{SongSynchronizer, SongDirectoryWatchDog}

trait SongModule extends AppSettings{

  lazy val songService: SongService = null
  private val songParsers = List(new TxtSongParser)

  lazy val songWatchDog = new SongDirectoryWatchDog(songsDir, songParsers)

  lazy val songSynchronizer = new SongSynchronizer(songWatchDog, songService, songParsers)
  lazy val loadSongsTask = new LoadSongsTask(songSynchronizer, songService)
}
