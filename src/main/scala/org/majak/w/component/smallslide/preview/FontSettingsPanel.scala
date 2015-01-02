package org.majak.w.component.smallslide.preview

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.{BoxPane, Button, ButtonPressListener, PushButton}
import org.majak.w.rx.{UiEvent, ObservableView}
import org.majak.w.ui.pivot.PivotComponent
import rx.lang.scala.Observable
import scala.collection.JavaConversions._

case object IncreaseFont extends UiEvent

case object DecreaseFont extends UiEvent

case object AlignCenter extends UiEvent

case object AlignLeft extends UiEvent

case object AlignRight extends UiEvent

case object VAlignCenter extends UiEvent

case object VAlignTop extends UiEvent

case object VAlignBottom extends UiEvent

class FontSettingsPanel extends PivotComponent with ObservableView {

  @BXML var fontPanel: BoxPane = _

  @BXML var biggerSizeButton: PushButton = _
  @BXML var smallerSizeButton: PushButton = _

  @BXML var alignLeftButton: PushButton = _
  @BXML var alignRightButton: PushButton = _
  @BXML var alignCenterButton: PushButton = _

  @BXML var valignTopButton: PushButton = _
  @BXML var valignBottomButton: PushButton = _
  @BXML var valignCenterButton: PushButton = _

  override protected def onUiBind(): Unit = {

    fontPanel.iterator.foreach(_.setPreferredWidth(PreviewSmallSlide.FONT_SETTING_PANEL_HEIGHT))

    biggerSizeButton.getButtonPressListeners.add(UiEventButtonPressListener(IncreaseFont))
    smallerSizeButton.getButtonPressListeners.add(UiEventButtonPressListener(DecreaseFont))

    alignLeftButton.getButtonPressListeners.add(UiEventButtonPressListener(AlignLeft))
    alignCenterButton.getButtonPressListeners.add(UiEventButtonPressListener(AlignCenter))
    alignRightButton.getButtonPressListeners.add(UiEventButtonPressListener(AlignRight))

    valignTopButton.getButtonPressListeners.add(UiEventButtonPressListener(VAlignTop))
    valignBottomButton.getButtonPressListeners.add(UiEventButtonPressListener(VAlignBottom))
    valignCenterButton.getButtonPressListeners.add(UiEventButtonPressListener(VAlignCenter))

  }

  lazy val fontSizeSubject = createUiEventSubject[UiEvent]

  override def observable: Observable[UiEvent] = fontSizeSubject

  case class UiEventButtonPressListener(event: UiEvent) extends ButtonPressListener {
    override def buttonPressed(button: Button): Unit = fontSizeSubject.onNext(event)
  }

}
