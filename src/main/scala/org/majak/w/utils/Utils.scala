package org.majak.w.utils

object Utils {
  /**
   * Creates option of type T from any ref
   * @param any
   * @tparam T
   * @return [[Option]]
   */
  def nullAsOption[T](any: AnyRef): Option[T] = if (any == null) Some(any.asInstanceOf[T]) else None

}
