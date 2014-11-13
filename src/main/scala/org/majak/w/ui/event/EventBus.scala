package org.majak.w.ui.event

import scala.collection.mutable.Map
import scala.collection.mutable.ListBuffer

class EventBus {

  protected val handlersMap = Map[Class[_], ListBuffer[Handler]]()

  def fire[H <: Handler](event: Event[H]) {
    val handlers = getHandlerList[H](event.getClass())
    handlers foreach (event.dispatch(_))
  }

  protected def getHandlerList[H](eventClass: Class[_]): ListBuffer[H] = {
    handlersMap.getOrElse(eventClass, ListBuffer()).asInstanceOf[ListBuffer[H]]
  }

  def addHandler[H <: Handler](eventClass: Class[_], handler: H) {
    val hList = getHandlerList[Handler](eventClass) += handler
    handlersMap += (eventClass -> hList)
  }

  def removeHandler[H <: Handler](eventClass: Class[_], handler: H) {
    val handlers = getHandlerList[H](eventClass) -= handler
    if (handlers.isEmpty) {
      handlersMap -= eventClass
    }
  }
}