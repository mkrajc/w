package org.majak.w.di

import org.majak.w.component.{WMainContentContainer, WMainMenu}


trait UiModule extends Module{
  val wMainMenu = new WMainMenu
  val wMainContentContainer = new WMainContentContainer
}
