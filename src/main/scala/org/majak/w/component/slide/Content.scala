package org.majak.w.component.slide

import org.apache.pivot.wtk.media.Image


trait Content
trait Front extends Content
trait Back extends Content

case object EmptyFront extends Front
case object EmptyBack extends Back
case object Empty extends Content

case class ImageContent(img: Image) extends Back
case class TextContent(texts: List[String]) extends Front
