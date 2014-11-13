package org.majak.w.ui

import org.apache.pivot.wtk.DesktopApplicationContext
import org.apache.pivot.wtk.Application
import org.apache.pivot.wtk.Display
import org.apache.pivot.collections.Map
import org.apache.pivot.wtk.Window
import org.majak.w.model.SongListItem
import org.majak.w.ui.component.songlist.SongListComponent

object ListVTest {
  
  val x = new Application.Adapter {
    override def startup(display: Display, properties: Map[String, String]){
      val w = new Window
      val listv = new SongListComponent
      val songs = List(SongListItem(1,"hello","asdf"), SongListItem(2,"hello2","asdf"))
      listv.bind
      listv.showData(songs)
      w setContent listv.asComponent
      w open display
    }
  }
  
  def main(args: Array[String]) {
    DesktopApplicationContext.main(x.getClass(), args);
  }
}