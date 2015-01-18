package org.majak.w.service

import org.apache.lucene.document.{Document, Field, TextField}
import org.apache.lucene.store.RAMDirectory
import org.junit.runner.RunWith
import org.majak.w.model.song.data.SongModel.{Song, SongPart}
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

  it should "save more songs" in {
    val service = new TLuceneSongService

    val song1 = Song(id = "aaa", name = "AAA", parts = Nil)
    val song2 = Song(id = "bbb", name = "BBB", parts = Nil)

    service.save(song1)
    service.save(song2)

    val s = service.songs

    s.size shouldEqual 2
    s(0) shouldEqual song1
    s(1) shouldEqual song2

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

    val s = service.findById("bbb")
    assert(s.isEmpty)
  }

  it should "save song with parts" in {
    val service = new TLuceneSongService
    val song = Song(id = "aaa", name = "AAA", parts = List(SongPart(List("a", "b")), SongPart(List("c"))))

    service.save(song)

    val s = service.songs

    s.size shouldEqual 1
    s(0) shouldEqual song
    s(0).parts.size shouldEqual 2

  }

}
