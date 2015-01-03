package org.majak.w.component.smallslide.preview

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.Menu.{Item, Section}
import org.apache.pivot.wtk._
import org.majak.w.rx.{ObservableView, UiEvent}
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

  private var removed: List[PushButton] = Nil
  private val arrow: MenuButton = new MenuButton()

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

    arrow.setMenu(new Menu)
    arrow.getMenu.getSections.add(new Section)
 }


  def compressToWidth(width: Int): Unit = {
    def removeUntilFit(currentWidth: Int, removed: List[PushButton]): List[PushButton] = {
      if (currentWidth > width) {
        val lastIndex = fontPanel.getLength - 1
        val comp = fontPanel.get(lastIndex).asInstanceOf[PushButton]
        fontPanel.remove(comp)
        removeUntilFit(currentWidth - comp.getPreferredWidth, comp :: removed)
      } else {
        removed
      }
    }

    def addUntilFit(currentWidth: Int, removed: List[PushButton]): List[PushButton] = {
      if (removed.nonEmpty) {
        val comp = removed.head
        if (currentWidth + comp.getPreferredWidth < width) {
          fontPanel.add(comp)
          addUntilFit(currentWidth + comp.getPreferredWidth, removed.tail)
        } else {
          removed
        }
      } else {
        removed
      }
    }

    // remove arrow if present
    fontPanel.remove(arrow)

    if (fontPanel.getPreferredWidth > width) {
      removed = removeUntilFit(fontPanel.getPreferredWidth, removed)
    } else {
      removed = addUntilFit(fontPanel.getPreferredWidth, removed)
    }

    if (removed.nonEmpty) {
      val section = arrow.getMenu.getSections.get(0)
      section.remove(0, section.getLength)

      arrow.setPreferredWidth(width - fontPanel.getPreferredWidth)
      fontPanel.add(arrow)
      removed.map(d => {
        val item = new Item(d.getButtonData)
        d.getButtonPressListeners.toList.foreach(item.getButtonPressListeners.add)
        section.add(item)
      })
    }
  }

  lazy val fontSizeSubject = createUiEventSubject[UiEvent]

  override def observable: Observable[UiEvent] = fontSizeSubject

  case class UiEventButtonPressListener(event: UiEvent) extends ButtonPressListener {
    override def buttonPressed(button: Button): Unit = fontSizeSubject.onNext(event)
  }

}
