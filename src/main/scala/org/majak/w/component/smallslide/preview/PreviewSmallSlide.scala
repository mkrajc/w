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

  private val fontPanel: FontSettingsPanel = new FontSettingsPanel

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
      case _ => ()
    }
  }

  override protected def onUiBind() = {
    super.onUiBind()

    fontPanel.bindView
    fontPanel.observable.subscribe(e => handleEvent(e))

    slide.getComponentMouseButtonListeners.add(new ComponentMouseButtonListener.Adapter {
      override def mouseClick(component: Component, button: Mouse.Button, x: Int, y: Int, count: Int): Boolean = {
        if (count >= 2) {
          confirm()
        }
        false
      }
    })

    slidePanel.getComponentListeners.add(new ComponentListener.Adapter {
      override def sizeChanged(component: Component, previousWidth: Int, previousHeight: Int): Unit = {
        layoutSlide()
        fontPanel.compressToWidth(slidePanel.getWidth)
        fontPanel.asComponent.setSize(slidePanel.getWidth, PreviewSmallSlide.FONT_SETTING_PANEL_HEIGHT)


      }
    })

    fontSettingsButton.setPreferredHeight(PreviewSmallSlide.FONT_SETTING_PANEL_HEIGHT)
    fontSettingsButton.setToggleButton(true)

    fontSettingsButton.getButtonStateListeners.add(new ButtonStateListener {
      override def stateChanged(button: Button, previousState: State): Unit = {
        button.getState match {
          case State.SELECTED => showFontSettings()
          case State.UNSELECTED => hideFontSettings()
          case _ => ()
        }
      }
    })

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

  private def showFontSettings(): Unit = {
    val fontSettingsPanelComp = fontPanel.asComponent
    slidePanel.add(fontSettingsPanelComp)

    fontSettingsPanelComp.setSize(slidePanel.getWidth, PreviewSmallSlide.FONT_SETTING_PANEL_HEIGHT)
    fontSettingsPanelComp.setLocation(0, 0)

    val t = new FadeInTransition(fontSettingsPanelComp, 300)
    t.startAndRemove()
  }

  private def hideFontSettings(): Unit = {
    val fontSettingsPanelComp = fontPanel.asComponent
    val t = new FadeOutTransition(fontSettingsPanelComp, 300)

    t.startAndRemove(new TransitionListener {
      override def transitionCompleted(transition: Transition): Unit = slidePanel.remove(fontSettingsPanelComp)
    })
  }

  override protected def setupSlide(slide: Slide): Unit = {
    slidePanel add slide
  }
}

object PreviewSmallSlide {
  val FONT_SETTING_PANEL_HEIGHT = 30
}