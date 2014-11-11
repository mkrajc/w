package org.majak.w

import org.apache.pivot.beans.BXML
import org.apache.pivot.beans.BXMLSerializer
import org.apache.pivot.collections.Map
import org.apache.pivot.wtk.Application
import org.apache.pivot.wtk.DesktopApplicationContext
import org.apache.pivot.wtk.Display
import org.apache.pivot.wtk.Label
import org.apache.pivot.wtk.Window
import org.majak.w.ui.Binding

class WApplication extends Application.Adapter with Binding {
  
  @BXML
  var wlabel: Label = _

  override def startup(display: Display, properties: Map[String, String]) = {
    val window = bind(classOf[Window])
    window open display
  }

}

object WApplication {
  def main(args: Array[String]) {
    DesktopApplicationContext.main(classOf[WApplication], args);
  }
}