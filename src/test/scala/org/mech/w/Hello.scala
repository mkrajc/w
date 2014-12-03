package org.mech.w

import java.awt.{Font, GraphicsEnvironment}

import org.apache.pivot.collections.Map
import org.apache.pivot.wtk.{Application, Button, ButtonPressListener, DesktopApplicationContext, Display, HorizontalAlignment, Label, PushButton, VerticalAlignment, Window}
import org.apache.pivot.wtk.effects.{Transition, TransitionListener}

@Deprecated
// just for starter
class Hello extends Application {

  val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
  val devices = ge.getScreenDevices()
  val screenConfigurations = devices map (_.getDefaultConfiguration())
  val controlWindow = new Window

  var collapseTransition: CollapseTransition = null

  private def setup() = {

    val b = new PushButton("create 2nd window")
    b.getButtonPressListeners().add(new ButtonPressListener {
      def buttonPressed(button: Button) = {
        val gc = screenConfigurations.filter(g => g != controlWindow.getDisplay().getDisplayHost().getGraphicsConfiguration())(0)
        val bounds = gc.getBounds()
        val w = new java.awt.Window(controlWindow.getDisplay().getHostWindow(), gc)

        println(gc.getDevice().getIDstring() + " " + w)

        val d = DesktopApplicationContext.createDisplay(bounds.width, bounds.height, w.getX, w.getY, false, true, true, controlWindow.getDisplay().getHostWindow(), null)
        val presentWindow = new Window
        presentWindow open d
        presentWindow.setMaximized(true);

        val label = new Label

        val text = "What if we dont want project D and its dependencies to be added to Project A's classpath because we know some of Project-D's dependencies (maybe Project-E for example) was missing from the repository, and you don't need/want the "

        label.setText(text)
        label.getStyles().put("font", new Font("Arial", Font.BOLD, 80));
        label.getStyles().put("horizontalAlignment",
          HorizontalAlignment.CENTER);
        label.getStyles().put("verticalAlignment",
          VerticalAlignment.CENTER);
        label.getStyles().put("wrapText",
          true);
        label.setHeight(bounds.height)
        label.getStyles().put("padding", "{top:40, left:30, bottom:40, right:30}");

        if (collapseTransition == null) {
          collapseTransition = new CollapseTransition(label, 2000, 30)

          val transitionListener = new TransitionListener() {
            def transitionCompleted(transition: Transition) = {
              val collapseTransition = transition.asInstanceOf[CollapseTransition]

              Hello.this.collapseTransition = null
              collapseTransition.end()
            }
          };

          collapseTransition.start(transitionListener)
        }

        presentWindow setContent label

        println("width:" + bounds.width + " labelWidth: " + label.getBounds().width)
      }
    })

    controlWindow setContent (b)

  }

  def startup(display: Display, properties: Map[String, String]): Unit = {
    val label = new Label("Hello")

    label.getStyles().put("horizontalAlignment",
      HorizontalAlignment.CENTER);
    label.getStyles().put("verticalAlignment",
      VerticalAlignment.CENTER);

    controlWindow.setPreferredWidth(300)

    controlWindow setContent label
    controlWindow.setMaximized(true);

    controlWindow open display

    controlWindow setTitle controlWindow.getDisplay().getDisplayHost().getGraphicsConfiguration().getDevice().getIDstring()

    setup
  }

  def shutdown(optional: Boolean): Boolean = {
    controlWindow.close
    false
  }

  def resume(): Unit = ???
  def suspend(): Unit = ???



}

object Hello {
  def main(args: Array[String]) {
    DesktopApplicationContext.main(classOf[Hello], args)
  }
}