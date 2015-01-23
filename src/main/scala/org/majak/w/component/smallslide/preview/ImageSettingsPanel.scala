package org.majak.w.component.smallslide.preview

import java.awt.Color

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk._
import org.majak.w.component.slide.Settings
import org.majak.w.rx.UiEvent
import rx.lang.scala.Observable

sealed trait ImageSettingsPanelUiEvent extends UiEvent

case class ChangeColor(color: Color) extends ImageSettingsPanelUiEvent

case class StretchImage(stretch: Boolean) extends ImageSettingsPanelUiEvent


class ImageSettingsPanel() extends SettingsPanel {
  @BXML protected var imagePanel: BoxPane = _
  @BXML protected var colorButton: ColorChooserButton = _
  @BXML protected var stretchBox: Checkbox = _


  lazy val imageSettingSubject = createUiEventSubject[ImageSettingsPanelUiEvent]

  override def observable: Observable[ImageSettingsPanelUiEvent] = imageSettingSubject

  override protected def panel: BoxPane = imagePanel

  override protected def onUiBind(): Unit = {
    super.onUiBind()

    colorButton.getColorChooserButtonSelectionListeners.add(new ColorChooserButtonSelectionListener {
      override def selectedColorChanged(colorChooserButton: ColorChooserButton, previousSelectedColor: Color): Unit = {
        imageSettingSubject.onNext(ChangeColor(colorChooserButton.getSelectedColor))
      }
    })

    stretchBox.getButtonPressListeners.add(new ButtonPressListener {
      override def buttonPressed(button: Button): Unit = {
        imageSettingSubject.onNext(StretchImage(button.isSelected))
      }
    })
  }

  override def init(settings: Settings): Unit = {
    colorButton.setSelectedColor(settings.image.background)
    stretchBox.setSelected(settings.image.stretch)
  }
}
