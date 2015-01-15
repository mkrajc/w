package org.majak.w.model.image.data

import java.io.File

import org.apache.pivot.wtk.media.Image
import org.majak.w.controller.watchdog.FileData


case class Thumbnail(source: FileData, thumbImage: Image) {

   def loadImage(): Image = Image.load(new File(source.path).toURI.toURL)

   override def equals(o: scala.Any): Boolean = {
     o match {
       case Thumbnail(src, _) => src == source
       case _ => false
     }
   }
 }
