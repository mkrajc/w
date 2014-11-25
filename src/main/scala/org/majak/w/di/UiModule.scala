package org.majak.w.di

import org.majak.w.component.WMainMenu


trait UiModule extends Module{
  val wMainMenu = new WMainMenu
}
