package org.majak.w

import org.apache.pivot.beans.BXML
import org.apache.pivot.collections.Map
import org.apache.pivot.wtk.{Application, DesktopApplicationContext, Display, Label, Window}
import org.majak.w.ui.pivot.Binding

class WApplication extends Application.Adapter with Binding[Window] {
  
  @BXML
  var wlabel: Label = _

  override def startup(display: Display, properties: Map[String, String]) = {
    bindUi
    root open display
  }

}

object WApplication {
  def main(args: Array[String]) {
    DesktopApplicationContext.main(classOf[WApplication], args);
  }
}