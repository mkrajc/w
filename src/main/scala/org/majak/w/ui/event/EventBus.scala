package org.majak.w.ui.event

import scala.collection.mutable.{ListBuffer, Map}

/**
 *
 * Event bus is designed for comunication of different system components.
 * Usage of event bus results in independence of different system components and loose coupling.
 *
 */
class EventBus {

  protected val handlersMap = Map[Class[_], ListBuffer[Handler]]()

  /**
   * Fires event to bus and notifies all registered handlers for given event type.
   *
   * @param event event to propagate
   * @tparam H handler type
   */
  def fire[H <: Handler](event: Event[H]) {
    val handlers = getHandlerList[H](event.getClass)
    handlers foreach event.dispatch
  }

  protected def getHandlerList[H](eventClass: Class[_]): ListBuffer[H] = {
    handlersMap.getOrElse(eventClass, ListBuffer()).asInstanceOf[ListBuffer[H]]
  }

  /**
   * Adds handler instance to event bus
   *
   * @param eventClass type of event
   * @param handler handler instance
   * @tparam H handler type
   */
  def addHandler[H <: Handler](eventClass: Class[_], handler: H) {
    val hList = getHandlerList[Handler](eventClass) += handler
    handlersMap += (eventClass -> hList)
  }

  /**
   * Removes handler instance from event bus
   *
   * @param eventClass type of event
   * @param handler handler instance
   * @tparam H handler type
   */
  def removeHandler[H <: Handler](eventClass: Class[_], handler: H) {
    val handlers = getHandlerList[H](eventClass) -= handler
    if (handlers.isEmpty) {
      handlersMap -= eventClass
    }
  }
}