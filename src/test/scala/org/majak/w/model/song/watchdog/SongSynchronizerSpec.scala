package org.majak.w.model.song.watchdog

import java.io.InputStream

import org.junit.runner.RunWith
import org.majak.w.TestableDir
import org.majak.w.controller.watchdog.FileData
import org.majak.w.di.Module
import org.majak.w.model.song.data.SongModel.{Song, SongPart}
import org.majak.w.model.song.parser.SongParser
import org.majak.w.model.song.service.SongService
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}
import org.slf4j.LoggerFactory

import scala.io.Source

@RunWith(classOf[JUnitRunner])
class SongSynchronizerSpec extends FlatSpec with Matchers with Module with TestableDir {

  val logger = LoggerFactory.getLogger(getClass)

  class XSongParser extends SongParser {
    override def ext: String = "x"

    override def parse(inputStream: InputStream): Song = {
      val lines = Source.fromInputStream(inputStream).getLines().toList
      val parts = lines.map(l => SongPart(List(l)))
      Song(inputStream.hashCode(), inputStream.hashCode().toString, parts)
    }
  }

  class TestSongService extends SongService {
    var removed = 0
    var saved = 0

    var s: List[Song] = Nil

    override def remove(song: Song): Boolean = {
      removed = removed + 1
      true
    }

    override def findByFileData(fileData: FileData): Option[Song] = {
      None
    }

    override def songs: List[Song] = s

    override def save(song: Song): Song = {
      saved = saved + 1
      this.s = song :: s
      song
    }
  }

  val parsers = List(new XSongParser)

  "SongSynchronizerSpec" should "remove delegate to service" in {
    val d = tmpRandDir
    testInTestDir(d) {
      f =>
        val service = new TestSongService
        val swd = new SongDirectoryWatchDog(f, parsers)
        val ss = new SongSynchronizer(swd, service, parsers)

        ss.remove(null)

        assert(service.removed == 1)
    }
  }

  it should "add delegate to service" in {
    val d = tmpRandDir
    testInTestDir(d) {
      f =>
        val service = new TestSongService
        val swd = new SongDirectoryWatchDog(f, parsers)
        val ss = new SongSynchronizer(swd, service, parsers)

        ss.add(null)

        assert(service.saved == 1)
    }
  }

  it should "parse song if had parser" in {
    val d = tmpRandDir
    testInTestDir(d) {
      f =>
        val service = new TestSongService
        val swd = new SongDirectoryWatchDog(f, parsers){
          override val indexFile = tmpRandFile
        }
        val ss = new SongSynchronizer(swd, service, parsers)

        prepareFilesInDir(f, Map("a.x"->"1\n2\n3\n4"))

        ss.sync()

        val song = service.songs.head
        song shouldNot be(null)
        song.parts.size shouldEqual 4
    }
  }
}
