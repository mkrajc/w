package org.majak.w

import org.apache.pivot.beans.BXML
import org.apache.pivot.beans.BXMLSerializer
import org.apache.pivot.collections.Map
import org.apache.pivot.wtk.Application
import org.apache.pivot.wtk.DesktopApplicationContext
import org.apache.pivot.wtk.Display
import org.apache.pivot.wtk.Label
import org.apache.pivot.wtk.Window

class WApplication extends Application.Adapter {

  @BXML
  var wlabel: Label = _

  override def startup(display: Display, properties: Map[String, String]): Unit = {
    val bxmlSerializer = new BXMLSerializer
    val window = bxmlSerializer.readObject(classOf[WApplication], "wapplication.xml").asInstanceOf[Window]
    bxmlSerializer.bind(this, classOf[WApplication]);
    
    window open display
  }

}

object WApplication {
  def main(args: Array[String]) {
    DesktopApplicationContext.main(classOf[WApplication], args);
  }
}