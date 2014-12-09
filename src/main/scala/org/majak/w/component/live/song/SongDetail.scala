package org.majak.w.component.live.song

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.Keyboard.{KeyCode, KeyLocation}
import org.apache.pivot.wtk._
import org.majak.w.model.SongModel.SongPart
import org.majak.w.ui.pivot.{StylesUtils, PivotComponent}
import org.majak.w.utils.ListsUtils

class SongDetail extends PivotComponent with SongDetailView {

  @BXML var songName: Label = _

  @BXML var songPartsPanel: BoxPane = _

  var spHandlers: List[SongPartSelectedHandler] = Nil
  var selected: Option[SongPartTextArea] = None

  override def showSongName(name: String) = songName.setText(name)

  override def clearSong() = {
    songName.setText("")
    songPartsPanel.clear()
  }

  override def showSongParts(parts: List[SongPart]) = {
    parts.foreach(p => songPartsPanel.add(createSongPartComponent(p)))

    songPartsPanel.getComponentKeyListeners.add(new ComponentKeyListener.Adapter {
      override def keyPressed(component: Component, keyCode: Int, keyLocation: KeyLocation): Boolean = {
        val currentIndex = selected.map(t => songPartsPanel.indexOf(t)).getOrElse(0)
        keyCode match {
          case KeyCode.UP => println("up")
          case KeyCode.DOWN => println("down")
          case _ => println("none")
        }
        false
      }
    })

    def select(spTextArea: SongPartTextArea) = {
      spHandlers.map(_(spTextArea.songPart))
      selected.map(ta => StylesUtils.setBackground(ta, "#ffffff"))
      selected = Some(spTextArea)
      StylesUtils.setBackground(spTextArea, "#f4f4f4")
    }

    def createSongPartComponent(sp: SongPart): SongPartTextArea = {
      val ta = new SongPartTextArea(sp)

      ta.setText(sp.lines.mkString("\n"))
      ta.getComponentMouseButtonListeners.add(new ComponentMouseButtonListener.Adapter {
        override def mouseClick(component: Component, button: Mouse.Button, x: Int, y: Int, count: Int): Boolean = {
          select(ta)
          false
        }
      })

      ta
    }
  }

  override def addSongPartSelectedHandler(h: SongPartSelectedHandler) = spHandlers = ListsUtils.add(h,spHandlers)
  override def removeSongPartSelectedHandler(h: SongPartSelectedHandler) = spHandlers = ListsUtils.delete(h,spHandlers)
}

class SongPartTextArea(val songPart: SongPart) extends TextArea {
  setEditable(false)
  setCursor(Cursor.HAND)
}
