package org.majak.w.ui.component.pivot

import org.apache.pivot.wtk.Keyboard.{KeyCode, KeyLocation}
import org.apache.pivot.wtk.TextInputContentListener.Adapter
import org.apache.pivot.wtk._
import org.majak.w.rx.{ObservableView, UiEvent}
import org.majak.w.ui.pivot.StylesUtils
import rx.lang.scala

sealed trait SearchBoxEvent extends UiEvent

case object CancelSearch extends SearchBoxEvent

case class Search(text: String) extends SearchBoxEvent


class SearchBox extends TextInput with ObservableView {

  StylesUtils.setBackground(this, "#ffffff")

  private lazy val events = createUiEventSubject[SearchBoxEvent]
  override def observable: scala.Observable[SearchBoxEvent] = events

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
    override def textChanged(textInput: TextInput): Unit = {
      if (textInput.getText == "") events.onNext(CancelSearch)
      else events.onNext(Search(textInput.getText))
    }
  })

}
