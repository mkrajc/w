package org.majak.w.component.live.screen

import org.majak.w.component.image.ImageLibraryView
import org.majak.w.component.live.song.SongPanelView
import org.majak.w.component.smallslide.live.LiveSmallSlideView
import org.majak.w.component.smallslide.preview.PreviewSmallSlideView
import org.majak.w.ui.mvp.View

trait LiveScreenView extends View {
  def setSongPanel(songPanelView: SongPanelView)
  def setImageLibrary(imageLibraryView: ImageLibraryView)
  def setLiveSmallSlideView(view: LiveSmallSlideView)
  def setPreviewSmallSlideView(view: PreviewSmallSlideView)
}