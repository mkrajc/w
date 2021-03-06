package org.majak.w.component.song.detail

import org.apache.pivot.beans.BXML
import org.apache.pivot.wtk.Keyboard.{KeyCode, KeyLocation}
import org.apache.pivot.wtk._
import org.majak.w.model.song.data.SongModel.SongPart
import org.majak.w.ui.pivot.{PivotComponent, StylesUtils}

class SongDetail extends PivotComponent with SongDetailView {

  @BXML var songName: Label = _

  @BXML var songPartsPanel: BoxPane = _

  var selected: Option[SongPartTextArea] = None

  override def showSongName(name: String) = songName.setText(name)

  override def clearSong() = {
    songName.setText("")
    songPartsPanel.removeAll()
  }

  override def showSongParts(parts: List[SongPart]) = {
    songPartsPanel.removeAll()
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

      selected.map(ta => StylesUtils.setBackground(ta, "#ffffff"))
      selected = Some(spTextArea)
      StylesUtils.setBackground(spTextArea, "#f4f4f4")

      selectSongPart(spTextArea.songPart)
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
}

class SongPartTextArea(val songPart: SongPart) extends TextArea {
  setEditable(false)
  setCursor(Cursor.HAND)
}
