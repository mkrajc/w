package org.majak.w.service

import org.apache.lucene.document.{Field, TextField, Document}
import org.apache.lucene.store.RAMDirectory
import org.junit.runner.RunWith
import org.majak.w.controller.watchdog.FileData
import org.majak.w.model.song.data.SongModel.Song
import org.majak.w.model.song.service.LuceneSongService
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class LuceneSongServiceSpec extends FlatSpec with Matchers {

  class TLuceneSongService extends LuceneSongService {
    override protected val index = new RAMDirectory()

    // ram dir must be initialize by somethig put into index
    val d = new Document

    val iw = createIndexWriter
    d.add(new TextField("x", "x", Field.Store.YES))
    iw.addDocument(d)
    iw.close
  }


  it should "save and find new song" in {
    val service = new TLuceneSongService
    val song = Song(id = "aaa", name = "AAA", parts = Nil)

    service.save(song)
    val s = service.songs

    s.size shouldEqual 1
    s(0) shouldEqual song

  }

  it should "load same song on multiple saving" in {
    val service = new TLuceneSongService
    val song = Song(id = "aaa", name = "AAA", parts = Nil)

    service.save(song)
    service.save(song)
    service.save(song)

    val s = service.songs

    s.size shouldEqual 1
    s(0) shouldEqual song

  }

  it should "change song on multiple saving" in {
    val service = new TLuceneSongService

    val song = Song(id = "aaa", name = "AAA", parts = Nil)
    service.save(song)

    val song2 = Song(id = "aaa", name = "BBB", parts = Nil)
    service.save(song2)

    val s = service.songs

    s.size shouldEqual 1
    s(0) shouldEqual song2

  }

  it should "remove saved song" in {
    val service = new TLuceneSongService
    val song = Song(id = "aaa", name = "AAA", parts = Nil)
    service.save(song)

    val s1 = service.songs
    s1.size shouldEqual 1

    service.remove(song)
    val s = service.songs

    s.size shouldEqual 0
  }

  it should "not find song by made file data" in {
    val service = new TLuceneSongService
    val song = Song(id = "aaa", name = "AAA", parts = Nil)
    service.save(song)

    val s = service.findByFileData(FileData("path", "123", "name.ext", "ext"))
    assert(s.isEmpty)
  }

}
