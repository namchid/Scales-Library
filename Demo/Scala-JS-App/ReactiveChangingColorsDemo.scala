package example

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom
import rx._

import edu.depauw.scales.graphics._
import edu.depauw.scales.reactive._
import edu.depauw.scales.music._
import Base._


object ScalaJSExample extends js.JSApp {
  def main(): Unit = {

    val playground = dom.document.getElementById("playground")    
    val paragraph = dom.document.createElement("p")
    paragraph.innerHTML = "<strong>It works!</strong>"
    dom.document.getElementById("playground").appendChild(paragraph)



    import CanvasHandler._
    import Reactive._

    def fnBlueTime(t: Double): Graphic = {
      val idNum = getId("blues")

      if(t.toInt % 2 == 0) {
        setFillColor(idNum, "rgb(0, 66, 107)")
        setStrokeColor(idNum, "rgb(73, 141, 171)")
      }
      else {
        setFillColor(idNum, "rgb(73, 141, 171)")
        setStrokeColor(idNum, "rgb(0, 66, 107)")
      }
      Rectangle(75, 75).translate(100, 100)
    }

    def fnChangeShape(t: Double): Graphic = {
      val idNum = getId("shaper")

      if(t.toInt % 3 == 0) {
        setFillColor(idNum, "green")
        Ellipse(37.5, 37.5).translate(200, 100)
      } else if(t.toInt % 2 == 0) {
        // setFillColor(idNum, "rgb(211, 74, 10)")
        setFillColor(idNum, "yellow")
        Rectangle(56.25, 56.25).translate(200, 100)
      } else {
        setFillColor(idNum, "rgb(234, 63, 11)")
        Rectangle(75, 75).translate(200, 100)
      }
    }

    def fnMove(t: Double): Graphic = {
      val idNum = getId("mover")

      if(t.toInt < 5) {
        setFillColor(idNum, "rgb(211, 139, 105)")
      } else if(t.toInt < 10) {
        setFillColor(idNum, "purple")
      } else if(t.toInt < 15) {
        setFillColor(idNum, "rgb(15, 25, 160)")
      } else {
        setFillColor(idNum, "rgb(20, 166, 25)")
      }
      Ellipse(70, 40).translate(t * 10, 200)
    }


    Reactor(ClockTick(1, 20), fnBlueTime, "grey", "black", 5, "blues")
    Reactor(ClockTick(1, 21), fnChangeShape, "grey", "black", 5, "shaper")
    Reactor(ClockTick(1, 20), fnMove, "grey", "white", 0, "mover")


  }
}
