package org.majak.w.ui

import org.apache.pivot.wtk.DesktopApplicationContext
import org.apache.pivot.wtk.Application
import org.apache.pivot.wtk.Display
import org.apache.pivot.collections.Map
import org.apache.pivot.wtk.Window

object ListVTest {
  
  val x = new Application.Adapter {
    override def startup(display: Display, properties: Map[String, String]){
      val w = new Window
      val listv = new ListV
      listv.bind 
      w setContent listv.r
      w open display
    }
  }
  
  def main(args: Array[String]) {
    DesktopApplicationContext.main(x.getClass(), args);
  }
}