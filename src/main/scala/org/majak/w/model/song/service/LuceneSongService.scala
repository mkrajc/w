package org.majak.w.model.song.service

import java.io.File

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.{Document, Field, StringField}
import org.apache.lucene.index.{DirectoryReader, IndexWriter, IndexWriterConfig, Term}
import org.apache.lucene.search._
import org.apache.lucene.store.{Directory, SimpleFSDirectory}
import org.apache.lucene.util.Version
import org.majak.w.controller.watchdog.FileData
import org.majak.w.di.AppSettings
import org.majak.w.model.song.data.SongModel.Song

class LuceneSongService extends SongService with AppSettings {

  val analyzer = new StandardAnalyzer()

  protected val index: Directory = new SimpleFSDirectory(new File(indexDir, "songdb"))

  private val FIELD_TYPE = "TYPE"
  private val FIELD_ID = "id"
  private val FIELD_NAME = "name"

  private val SONG_TYPE = "SONG"

  override def remove(song: Song): Boolean = {
    deleteDocs(idQuery(song.id))
    true
  }

  override def findByFileData(fileData: FileData): Option[Song] = {
    findById(fileData.md5hex)
  }

  override def songs: List[Song] = {
    listDoc(songQuery).map(docToSong)
  }

  protected val songQuery = new TermQuery(new Term(FIELD_TYPE, SONG_TYPE))

  protected def idQuery(id: String): Query = {
    val bq = new BooleanQuery()
    bq.add(songQuery, BooleanClause.Occur.MUST)
    bq.add(new TermQuery(new Term(FIELD_ID, id)), BooleanClause.Occur.MUST)
    bq
  }

  protected def docToSong(doc: Document): Song = {
    Song(name = doc.get(FIELD_NAME), parts = Nil, id = doc.get(FIELD_ID))
  }

  protected def songToDoc(song: Song): Document = {
    val document = new Document

    document.add(new StringField(FIELD_TYPE, SONG_TYPE, Field.Store.YES))
    document.add(new StringField(FIELD_ID, song.id, Field.Store.YES))
    document.add(new StringField(FIELD_NAME, song.name, Field.Store.YES))

    document
  }

  protected def createIndexWriter(): IndexWriter = {
    val config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer)
    new IndexWriter(index, config)
  }

  override def save(song: Song): Song = {
    val indexWriter = createIndexWriter()

    val d = findDocBySongId(song.id)

    // if exist delete doc
    if (d.isDefined) {
      indexWriter.deleteDocuments(idQuery(song.id))
    }

    // readd new document
    indexWriter.addDocument(songToDoc(song))

    indexWriter.close()

    song
  }

  override def findById(id: String): Option[Song] = {
    findDocBySongId(id).map(docToSong)
  }

  private def findDocBySongId(id: String): Option[Document] = {
    firstDoc(idQuery(id))
  }

  private def firstDoc(q: Query): Option[Document] = {
    val reader = DirectoryReader.open(index)
    val searcher = new IndexSearcher(reader)
    val topDocs = searcher.search(q, 1)
    if (topDocs.scoreDocs.nonEmpty) {
      val hit = topDocs.scoreDocs(0)
      val document = searcher.doc(hit.doc)
      Some(document)
    } else {
      None
    }
  }

  private def deleteDocs(q: Query) = {
    val indexWriter = createIndexWriter()
    indexWriter.deleteDocuments(q)
    indexWriter.close()
  }


  private def listDoc(q: Query): List[Document] = {
    val reader = DirectoryReader.open(index)
    val searcher = new IndexSearcher(reader)
    val topDocs = searcher.search(q, reader.numDocs())
    if (topDocs.scoreDocs.nonEmpty) {
      topDocs.scoreDocs.map(sd => searcher.doc(sd.doc)).toList
    } else {
      Nil
    }
  }
}
