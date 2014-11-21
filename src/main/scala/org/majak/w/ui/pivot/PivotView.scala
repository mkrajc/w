package org.majak.w.ui.pivot

import org.apache.pivot.wtk.Component
import org.majak.w.ui.mvp.View

/**
 * Associate [[View]] and pivot [[Component]].
 */
trait PivotView extends View {
  /**
   * Returns associated view's as [[org.apache.pivot.wtk.Component]]
   *
   * It can be same instance or different component
   */
  val asComponent: Component
}
