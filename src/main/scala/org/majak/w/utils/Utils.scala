package org.majak.w.utils

import java.text.Normalizer
import java.util.regex.Pattern

object Utils {
  val normalizerPattern = Pattern.compile("[^\\p{ASCII}]")

  /**
   * Creates option of type T from any ref
   * @param any
   * @tparam T
   * @return [[Option]]
   */
  def nullAsOption[T](any: AnyRef): Option[T] =
    if (any == null) None
    else Some(any.asInstanceOf[T])

  def normalizeAccentedText(txt: String): String =
    normalizerPattern.matcher(Normalizer.normalize(txt, Normalizer.Form.NFD)) replaceAll ""

  def extension(filename: String): String = {
    val lastDotIndex = filename.lastIndexOf(".")
    filename.substring(lastDotIndex + 1).toLowerCase
  }

  def okOrFailed(boolean: Boolean): String = {
    if (boolean) "OK" else "FAILED"
  }

  def boolToOpt(expr: Boolean): Option[Boolean] = {
    if (expr) Some(expr) else None
  }
}
