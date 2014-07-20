package example

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom
import rx._

import edu.depauw.scales.graphics._
import Base._

object Canvas {
  val canvas = dom.document.createElement("canvas").asInstanceOf[dom.HTMLCanvasElement]
  canvas.width = 500
  canvas.height = 500
  val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  val playground = dom.document.getElementById("playground")
  playground.appendChild(canvas)
}

object GraphicHolder {
  val ctx = Canvas.ctx
  val width = Canvas.canvas.width
  val height = Canvas.canvas.height

  val graphics: Var[List[Graphic]] = Var(Nil)
  val fillColors: Var[List[String]] = Var(Nil)
  val strokeStyles: Var[List[String]] = Var(Nil)
  val lineWidths: Var[List[Int]] = Var(Nil)

  def getIndex(): Int = {
    return graphics().size
  }

  def addGraphic(g: Graphic): Unit = { 
    graphics() = graphics() :+ g 
  }

  def setFillColor(index: Int, color: String): Unit = {
    fillColors() = fillColors().patch(index, List(color), 1)
  }

  def setStrokeColor(index: Int, color: String): Unit = {
    strokeStyles() = strokeStyles().patch(index, List(color), 1)
  }

  def setWidth(index: Int, width: Int): Unit = {
    lineWidths() = lineWidths().patch(index, List(width), 1)
  }

  def setAll(index: Int, fillColor: String, strokeColor: String, width: Int): Unit = {
    fillColors() = fillColors().patch(index, List(fillColor), 1)
    strokeStyles() = strokeStyles().patch(index, List(strokeColor), 1)
    lineWidths() = lineWidths().patch(index, List(width), 1)
  }

  def updateGraphic(index: Int, g: Graphic): Unit = {
    graphics() = graphics().patch(index, List(g), 1)
  }

  Obs(graphics) {
    ctx.clearRect(0, 0, width, height)
    for(i <- 0 until graphics().length) {
      ctx.fillStyle = fillColors()(i)
      ctx.strokeStyle = strokeStyles()(i)
      ctx.lineWidth = lineWidths()(i)
      graphics()(i).render(ctx)
    }
  }
}

/////////////////////////////////////////////////// subscriptions
object MouseClick {
  val xy = Var((0, 0))
  val x = Rx { xy()._1 }
  val y = Rx { xy()._2 }

  private def listen() = Rx {
    dom.onclick = { 
      (e: dom.MouseEvent) => xy() = (e.clientX.toInt, e.clientY.toInt)
    }
  }

  listen()

  def subscribe(): Rx[(Int, Int)] = xy
  def subscribeX(): Rx[Int] = x
  def subscribeY(): Rx[Int] = y
}

/////////////////////////////////////////////////// reactives
object Reactive {
  type ReactionType = Int

  final val ClockTick: ReactionType = 0
  final val MouseClick: ReactionType = 1
  final val MousePosition: ReactionType = 2

  final val MouseClickX: ReactionType = 3
  final val MouseClickY: ReactionType = 4
  final val MousePositionX: ReactionType = 5
  final val MousePositionY: ReactionType = 6

  final val ClockTickGetMousePosition: ReactionType = 7
  final val MouseClickGetClockTime: ReactionType = 8
  final val MousePositionGetClockTime: ReactionType = 9
}

case class Reactor[T](reaction: Reactive.ReactionType, fn: T => Graphic, fillStyle: String = "grey",
  strokeStyle: String = "black", width: Int = 1, framesPerSecond: Double = 1, duration: Double = 0) {

  val function = fn.asInstanceOf[(Any => Graphic)]

  val target: Rx[Any] = reaction match {
    case Reactive.MouseClick => MouseClick.subscribe
    case Reactive.MouseClickX => MouseClick.subscribeX
    case Reactive.MouseClickY => MouseClick.subscribeY
    case _ => Var(0)
  } 

  val index = Var(-1)

  private def initGraphic(g: Graphic): Unit = {
    GraphicHolder.setAll(index(), fillStyle, strokeStyle, width)
    GraphicHolder.addGraphic(g)
  }

  Obs(target) {
    val g = function(target())
    if(index() == -1) {
      index() = GraphicHolder.getIndex
      initGraphic(g)
    } else {
      GraphicHolder.updateGraphic(index(), g)
    }
  }
}


object ScalaJSExample extends js.JSApp {
  def main(): Unit = {
    val playground = dom.document.getElementById("playground")
    
    val paragraph = dom.document.createElement("p")
    paragraph.innerHTML = "<strong>It works!</strong>"
    dom.document.getElementById("playground").appendChild(paragraph)

/////////////////// tehehehehe
    def fnMouse(xy: (Int, Int)): Graphic = {
      Rectangle(100, 200).translate(xy._1, xy._2)
    }

    def fnMouseX(x: Int): Graphic = {
      Rectangle(50, 100).translate(x, 300)
    }

    def fnMouseY(y: Int): Graphic = {
      Rectangle(50, 100).translate(250, y)
    }

    Reactor(Reactive.MouseClick, fnMouse)
    Reactor(Reactive.MouseClickX, fnMouseX, "blue")
    Reactor(Reactive.MouseClickY, fnMouseY, "red")
  }
}
