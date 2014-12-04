package org.majak.w.component.live.slide

import org.apache.pivot.wtk.media.Image


trait Content
case class ImageContent(img: Image) extends Content
case class TextContent(texts: List[String]) extends Content
case class ClearTextContent() extends Content
case class ClearImageContent() extends Content
case class ClearContent() extends Content