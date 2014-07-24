package edu.depauw.scales.reactive

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom
import rx._

import edu.depauw.scales.graphics._

object Canvas {
  val canvas = dom.document.createElement("canvas").asInstanceOf[dom.HTMLCanvasElement]
  canvas.width = 500
  canvas.height = 500
  val ctx = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  val playground = dom.document.getElementById("playground")
  playground.appendChild(canvas)
}

object CanvasHandler {
  val ctx = Canvas.ctx
  val width = Canvas.canvas.width
  val height = Canvas.canvas.height

  val graphics: Var[List[Graphic]] = Var(Nil)
  val fillStyles: Var[List[String]] = Var(Nil)
  val strokeStyles: Var[List[String]] = Var(Nil)
  val lineWidths: Var[List[Int]] = Var(Nil)

  val ids: Var[Map[String, Int]] = Var(Map()) 

  def getIndex(): Int = {
    return graphics().size
  }

  def addGraphic(g: Graphic): Unit = { 
    graphics() = graphics() :+ g 
  }

  def setFillColor(index: Int, style: String): Unit = {
    fillStyles() = fillStyles().patch(index, List(style), 1)
  }

  def setStrokeColor(index: Int, style: String): Unit = {
    strokeStyles() = strokeStyles().patch(index, List(style), 1)
  }

  def setWidth(index: Int, width: Int): Unit = {
    lineWidths() = lineWidths().patch(index, List(width), 1)
  }

  def setId(index: Int, id: String): Unit = {
    ids() + (id -> index)
  }

  def getId(id: String): Int = {
    if(!ids().isDefinedAt(id)) return -1
    else ids()(id)
  }

  def setAll(index: Int, fillStyle: String, strokeStyle: String, width: Int, id: String = ""): Unit = {
    fillStyles() = fillStyles().patch(index, List(fillStyle), 1)
    strokeStyles() = strokeStyles().patch(index, List(strokeStyle), 1)
    lineWidths() = lineWidths().patch(index, List(width), 1)
    ids() + (id -> index)
  }

  def updateGraphic(index: Int, g: Graphic): Unit = {
    graphics() = graphics().patch(index, List(g), 1)
  }

  Obs(graphics) {
    ctx.clearRect(0, 0, width, height)
    for(i <- 0 until graphics().length) {
      ctx.fillStyle = fillStyles()(i)
      ctx.strokeStyle = strokeStyles()(i)
      ctx.lineWidth = lineWidths()(i)
      graphics()(i).render(ctx)
    }
  }
}