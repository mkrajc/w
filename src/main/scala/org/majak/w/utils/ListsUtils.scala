package org.majak.w.utils


object ListsUtils {
  def add[T](item: T, list: List[T]): List[T] = item :: list

  def delete[T](item: T, list: List[T]): List[T] = list diff List(item)
}
