package org.majak.w.component.smallslide.preview

import org.apache.pivot.wtk.Menu.{Section, Item}
import org.apache.pivot.wtk.{Menu, MenuButton, BoxPane, PushButton}
import org.majak.w.component.slide.Settings
import org.majak.w.rx.ObservableView
import org.majak.w.ui.pivot.{StylesUtils, PivotComponent}

import scala.collection.JavaConversions._

trait SettingsPanel extends PivotComponent with ObservableView {

  private var removed: List[PushButton] = Nil

  private val arrow: MenuButton = new MenuButton()
  arrow.setMenu(new Menu)
  arrow.getMenu.getSections.add(new Section)

  protected def panel: BoxPane
  def init(settings: Settings): Unit


  override protected def onUiBind(): Unit = {
    super.onUiBind
    StylesUtils.setBackground(panel, "#f3f3f3")
  }

  def compressToWidth(width: Int): Unit = {
    def removeUntilFit(currentWidth: Int, removed: List[PushButton]): List[PushButton] = {
      if (currentWidth > width) {
        val lastIndex = panel.getLength - 1
        val comp = panel.get(lastIndex)
        comp match {
          case pb: PushButton =>
            panel.remove(pb)
            removeUntilFit(currentWidth - pb.getPreferredWidth, pb :: removed)
          case _ => removed
        }
      } else {
        removed
      }
    }

    def addUntilFit(currentWidth: Int, removed: List[PushButton]): List[PushButton] = {
      if (removed.nonEmpty) {
        val comp = removed.head
        if (currentWidth + comp.getPreferredWidth < width) {
          panel.add(comp)
          addUntilFit(currentWidth + comp.getPreferredWidth, removed.tail)
        } else {
          removed
        }
      } else {
        removed
      }
    }

    // remove arrow if present
    panel.remove(arrow)

    if (panel.getPreferredWidth > width) {
      removed = removeUntilFit(panel.getPreferredWidth, removed)
    } else {
      removed = addUntilFit(panel.getPreferredWidth, removed)
    }

    if (removed.nonEmpty) {
      val section = arrow.getMenu.getSections.get(0)
      section.remove(0, section.getLength)

      arrow.setPreferredWidth(math.max(width - panel.getPreferredWidth, 0))
      panel.add(arrow)
      removed.map(d => {
        val item = new Item(d.getButtonData)
        d.getButtonPressListeners.toList.foreach(item.getButtonPressListeners.add)
        section.add(item)
      })
    }
  }
}
