package org.majak.w.component

import org.apache.pivot.beans.BXML
import org.apache.pivot.util.Resources
import org.apache.pivot.wtk.{BoxPane, PushButton}
import org.majak.w.ui.pivot.PivotComponent

class WMainMenu extends PivotComponent[BoxPane]{

  bindUi

  @BXML
  var liveButton: PushButton = _

  @BXML
  var songsButton: PushButton = _

}
