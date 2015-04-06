package org.majak.w.component.smallslide.preview

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.Button.State
import org.apache.pivot.wtk._
import org.apache.pivot.wtk.effects.{Transition, TransitionListener}
import org.majak.w.component.slide.Slide
import org.majak.w.component.smallslide.SmallSlide
import org.majak.w.rx.UiEvent
import org.majak.w.ui.pivot.effects.{FadeInTransition, FadeOutTransition}

class PreviewSmallSlide extends SmallSlide with PreviewSmallSlideView {

  @BXML var slidePanel: Panel = _

  @BXML var fontSettingsButton: PushButton = _
  @BXML var imageSettingsButton: PushButton = _

  private val fontPanel = new FontSettingsPanel
  private val imagePanel = new ImageSettingsPanel

  private var setttingsGroup: List[Button] = Nil

  def handleEvent(e: UiEvent): Unit = {
    e match {
      case IncreaseFont => slide.increaseFont()
      case DecreaseFont => slide.decreaseFont()
      case AlignCenter => slide.setHorizontalAlign(HorizontalAlignment.CENTER)
      case AlignLeft => slide.setHorizontalAlign(HorizontalAlignment.LEFT)
      case AlignRight => slide.setHorizontalAlign(HorizontalAlignment.RIGHT)
      case VAlignCenter => slide.setVerticalAlign(VerticalAlignment.CENTER)
      case VAlignTop => slide.setVerticalAlign(VerticalAlignment.TOP)
      case VAlignBottom => slide.setVerticalAlign(VerticalAlignment.BOTTOM)
      case FontFamilyChanged(family) => slide.setFontFamily(family)
      case ChangeColor(color) => slide.setColor(color)
      case StretchImage(stretch) => slide.setStretch(stretch)
      case _ => ()
    }
  }

  override protected def onUiBind() = {
    super.onUiBind()

    slide.getComponentMouseButtonListeners.add(new ComponentMouseButtonListener.Adapter {
      override def mouseClick(component: Component, button: Mouse.Button, x: Int, y: Int, count: Int): Boolean = {
        if (count >= 2) {
          confirm()
        }
        false
      }
    })

    bindButtonAndSetting(fontSettingsButton, fontPanel)
    bindButtonAndSetting(imageSettingsButton, imagePanel)

  }

  private def bindButtonAndSetting(button: PushButton, settingsPanel: SettingsPanel): Unit = {
    settingsPanel.bindView

    settingsPanel.observable.subscribe(e => handleEvent(e))

    slidePanel.getComponentListeners.add(new ComponentListener.Adapter {
      override def sizeChanged(component: Component, previousWidth: Int, previousHeight: Int): Unit = {
        settingsPanel.compressToWidth(slidePanel.getWidth)
        settingsPanel.asComponent.setSize(slidePanel.getWidth, PreviewSmallSlide.SETTING_PANEL_HEIGHT)
      }
    })

    setttingsGroup = button :: setttingsGroup

    button.setPreferredHeight(PreviewSmallSlide.SETTING_PANEL_HEIGHT)
    button.setToggleButton(true)
    button.getButtonStateListeners.add(new SettingsPanelButtonStateListener(settingsPanel))
    button.getButtonStateListeners.add(new ToggleButtonStateListener)

    settingsPanel.init(slide.settings)

  }

  private def layoutSlide(): Unit = {

    val h = if (slide.getPreferredHeight > 0) math.min(slide.getPreferredHeight, slidePanel.getHeight)
    else slidePanel.getHeight

    val w = if (slide.getPreferredWidth > 0) math.min(slide.getPreferredWidth, slidePanel.getWidth)
    else slidePanel.getWidth

    slide.setSize(w, h)

    val x = (slidePanel.getWidth - slide.getWidth) / 2
    val y = (slidePanel.getHeight - slide.getHeight) / 2

    slide.setLocation(x, y)
  }

  override protected def autoSizeSlideView(): Unit = {
    super.autoSizeSlideView()
    layoutSlide()
  }

  private def showSettings(settingsPanel: SettingsPanel): Unit = {
    val panel = settingsPanel.asComponent
    slidePanel.add(panel)

    panel.setSize(slidePanel.getWidth, PreviewSmallSlide.SETTING_PANEL_HEIGHT)
    panel.setLocation(0, 0)

    val t = new FadeInTransition(panel, 300)
    t.startAndRemove()
  }

  private def hideSettings(settingsPanel: SettingsPanel): Unit = {
    val panel = settingsPanel.asComponent
    val t = new FadeOutTransition(panel, 300)

    t.startAndRemove(new TransitionListener {
      override def transitionCompleted(transition: Transition): Unit = slidePanel.remove(panel)
    })
  }

  override protected def setupSlide(slide: Slide): Unit = {
    slidePanel add slide
  }


  class SettingsPanelButtonStateListener(val settingsPanel: SettingsPanel) extends ButtonStateListener {
    override def stateChanged(button: Button, previousState: State): Unit = {
      button.getState match {
        case State.SELECTED => showSettings(settingsPanel)
        case State.UNSELECTED => hideSettings(settingsPanel)
        case _ => ()
      }
    }
  }

  class ToggleButtonStateListener extends ButtonStateListener {
    override def stateChanged(sbutton: Button, previousState: State): Unit = {
      sbutton.getState match {
        case State.SELECTED =>
          setttingsGroup.foreach(b => if (sbutton != b) {
            b.setSelected(false)
          })
        case _ => ()
      }
    }
  }

}

object PreviewSmallSlide {
  val SETTING_PANEL_HEIGHT = 30
}
