package org.majak.w.component.smallslide.preview

import java.awt.Font

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.slide.Settings
import org.majak.w.rx.UiEvent
import org.majak.w.ui.component.pivot.FontListButton
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

case class FontFamilyChanged(family: String) extends UiEvent

class FontSettingsPanel extends SettingsPanel{

  @BXML protected var fontPanel: BoxPane = _

  @BXML protected var fontButton: FontListButton = _

  @BXML protected var biggerSizeButton: PushButton = _
  @BXML protected var smallerSizeButton: PushButton = _

  @BXML protected var alignLeftButton: PushButton = _
  @BXML protected var alignRightButton: PushButton = _
  @BXML protected var alignCenterButton: PushButton = _

  @BXML protected var valignTopButton: PushButton = _
  @BXML protected var valignBottomButton: PushButton = _
  @BXML protected var valignCenterButton: PushButton = _

  override def init(settings: Settings): Unit = {
    fontButton.selectFamily(settings.text.font.family)
  }

  override protected def onUiBind(): Unit = {
    super.onUiBind()

    fontPanel.iterator.foreach(_.setPreferredWidth(PreviewSmallSlide.SETTING_PANEL_HEIGHT))

    fontButton.setPreferredWidth(150)
    fontButton.getListButtonSelectionListeners.add(new ListButtonSelectionListener {
      override def selectedIndexChanged(listButton: ListButton, previousSelectedIndex: Int): Unit = ()

      override def selectedItemChanged(listButton: ListButton, previousSelectedItem: scala.Any): Unit = {
        fontSettingSubject.onNext(FontFamilyChanged(listButton.getSelectedItem.asInstanceOf[Font].getFamily))
      }
    })

    biggerSizeButton.getButtonPressListeners.add(UiEventButtonPressListener(IncreaseFont))
    smallerSizeButton.getButtonPressListeners.add(UiEventButtonPressListener(DecreaseFont))

    alignLeftButton.getButtonPressListeners.add(UiEventButtonPressListener(AlignLeft))
    alignCenterButton.getButtonPressListeners.add(UiEventButtonPressListener(AlignCenter))
    alignRightButton.getButtonPressListeners.add(UiEventButtonPressListener(AlignRight))

    valignTopButton.getButtonPressListeners.add(UiEventButtonPressListener(VAlignTop))
    valignBottomButton.getButtonPressListeners.add(UiEventButtonPressListener(VAlignBottom))
    valignCenterButton.getButtonPressListeners.add(UiEventButtonPressListener(VAlignCenter))

  }

  lazy val fontSettingSubject = createUiEventSubject[UiEvent]

  override def observable: Observable[UiEvent] = fontSettingSubject

  case class UiEventButtonPressListener(event: UiEvent) extends ButtonPressListener {
    override def buttonPressed(button: Button): Unit = fontSettingSubject.onNext(event)
  }

  override protected def panel: BoxPane = fontPanel
}


