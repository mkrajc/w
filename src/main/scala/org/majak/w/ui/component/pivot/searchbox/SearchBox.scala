package org.majak.w.ui.component.pivot.searchbox

import org.apache.pivot.wtk.Keyboard.{KeyCode, KeyLocation}
import org.apache.pivot.wtk.TextInputContentListener.Adapter
import org.apache.pivot.wtk._
import org.majak.w.ui.pivot.StylesUtils

import scala.collection.mutable.ListBuffer

class SearchBox extends TextInput {

  StylesUtils.setBackground(this, "#ffffff")

  private val searchHandlers = new ListBuffer[SearchHandler]

  def addSearchHandler(h: SearchHandler) = searchHandlers += h

  def removeSearchHandler(h: SearchHandler) = searchHandlers -= h


  getComponentKeyListeners.add(new ComponentKeyListener.Adapter {
    override def keyPressed(component: Component, keyCode: Int, keyLocation: KeyLocation): Boolean = {
      if (keyCode == KeyCode.ESCAPE) {
        setText("")
        true
      }
      else false
    }
  })

  getTextInputContentListeners.add(new Adapter {

    def notifyCancel = searchHandlers foreach (_.onSearchCancel())

    def notifySearch(text: String) = searchHandlers foreach (_.onSearch(text))

    override def textChanged(textInput: TextInput): Unit = {
      if (textInput.getText == "") notifyCancel
      else notifySearch(textInput.getText)
    }

  })

}

trait SearchHandler {
  def onSearch(text: String)

  def onSearchCancel()
}
