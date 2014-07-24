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

/////////////////// tehehehehe
    def fnMouse(xy: (Int, Int)): Graphic = {
      Rectangle(100, 200).translate(xy._1 - 25, xy._2 - 100)
    }

    def fnMouseX(x: Int): Graphic = {
      Rectangle(50, 100).r.translate(x + 25, 300)
    }

    def fnMouseY(y: Int): Graphic = {
      Rectangle(50, 100).b.translate(250, y - 100)
    }

    def fnTime(t: Double): Graphic = {
      (Ellipse(20, 30) atop Rectangle(40, 40)).translate(t * 30, 300)
    }

    def fnKey(x: Int): Graphic = {
      Ellipse(45, 60).translate(200, x * 30)
    }

    def fnKeyAny(x: Int): Graphic = {
      Ellipse(25, 25).translate(x * 10, x * 10)
    }

    val num = Var(0)
    def fnPos(xy: (Int, Int)): Graphic = {      
      val idNum = CanvasHandler.getId("reactor1")
      num() += 1
      if(num() % 2 == 0) CanvasHandler.setFillColor(idNum, "blue")
      else CanvasHandler.setFillColor(idNum, "magenta")

      Ellipse(25, 25).translate(xy._1 - 5, xy._2 - 155)
    }

    import Reactive._
    Reactor(Reactive.MousePosition, fnPos, "rgb(65, 123, 142)", "black", 3, "reactor1")
    
    // Reactor(MouseClick, fnMouse, "green")
    // Reactor(ClockTick(2, 20), fnTime, "red")
    // Reactor(KeyPress(Key.Space), fnKey, "orange", "grey", 5)
    // Reactor(KeyPress, fnKeyAny, "blue", "magenta")
   

    // Reactor(MouseClickX, fnMouseX, "blue")
    // Reactor(MouseClickY, fnMouseY, "rgb(70, 200, 70)")
    // Reactor(MousePositionX, fnMouseX, "rgb(133, 60, 200)")
    // Reactor(MousePositionY, fnMouseY, "rgb(155, 140, 10)")

    // Reactor(ClockTickGetMousePosition(1, 100), fnPos, "yellow", "green", 4)
    // Reactor(MouseClickGetClockTime, fnTime, "magenta", "grey", 3)
    // Reactor(MousePositionGetClockTime, fnTime, "rgb(200, 200, 55)", "white", 0)
    // Reactor(KeyPressGetClockTime(Key.A), fnTime, "black", "blue")
    // Reactor(KeyPressGetClockTime, fnTime, "green") //todo
    
    import Audio._
    
    def fnNote(x: Int): NoteSeq = {
      // val note1 = Note(NoRamp(1), x)
      // val note2 = Note(LinearRamp(0, 2), 400, 1)
      //NoteSeq(Note(NoRamp(1), x), Note(LinearRamp(0, 2), 400, 1))
      NoteSeq(Note(NoRamp, 222, 0, 1, 1), Note(ExponentialRamp(180), 150, 2))
    }

    def fnSingleNote(x: Int): Note = {
      Note(NoRamp, x, 0)
    }

    // Reactor(MouseClickX, fnNote)
    Reactor(MouseClickX, fnSingleNote)
    // Note(LinearRamp(0, 2), 400, 3)
    // Note(ExponentialRamp(200, 2), 170, 6)
    // Note(Beats(5, .5, .5), 300, 8)

  }
}
