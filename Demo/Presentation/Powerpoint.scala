package example

import scala.scalajs.js
import org.scalajs.dom
import rx._

import edu.depauw.scales.graphics._
import edu.depauw.scales.reactive._
import edu.depauw.scales.music._

trait FontType {
  val font: String = "serif"
  val fontSize: Int
  val color: Base.Color
}

case object Title extends FontType { 
  val fontSize = 48
  val color = Base.Color.HotPink
}

case object Subtitle extends FontType {
  val fontSize = 30
  val color = Base.Color.SeaGreen
}

case object RegularBullet extends FontType {
  val fontSize = 20
  val color = Base.Color.Black
}

case object Regular extends FontType {
  val fontSize = 20
  val color = Base.Color.Black
}

case class Custom(val f: String, val fontSize: Int, val color: Base.Color) extends FontType {
  override val font = f
}

class Powerpoint {
  import Base._

  // helper method for splitAll
  def takeWords(words: Array[String], graphic: Graphic, font: FontType): (Graphic, Array[String]) = words.length match {
  	case 0 => (graphic, words)
    case _ =>
      val newGraphic = graphic beside Text(" " + words(0), Font(font.font, font.fontSize), false).fill(font.color)
      if(newGraphic.bounds.width < Canvas.canvas.width - 40) return takeWords(words.tail, newGraphic, font)
      else return (graphic, words)
  }

  // for splitting text so that it fits on canvas
  def splitAll(words: Array[String], graphics: Array[Graphic], font: FontType): Array[Graphic] = words.length match {
    case 0 => graphics
    case _ =>
      val (g, rest) = takeWords(words, Text(""), font)
      splitAll(rest, graphics :+ g, font)
  }

  // joins all texts into one graphic
  def handleTexts(texts: Array[Graphic]): Graphic = texts.length match {
    case 1 => texts(0).tl
    case _ => texts(0).tl above handleTexts(texts.tail)
  }

  // converts a string to graphic
  def stringToGraphic(text: String, font: FontType = RegularBullet): Graphic = font match {
    case Title =>
      val arr = text.split(" ")
      val graphics = splitAll(arr, Array[Graphic](), Title)
      val g = handleTexts(graphics)
      g.tl.translate((Canvas.canvas.width - g.bounds.width) / 2, 0)
    case Subtitle =>
      val arr = text.split(" ")
      val graphics = splitAll(arr, Array[Graphic](), Subtitle)
      val g = handleTexts(graphics)
      g.tl.translate((Canvas.canvas.width - g.bounds.width) / 2, 0)
    case RegularBullet =>
      val bullet = Rectangle(10, 10).pad(1.5).tl 
      val arr = text.split(" ")
      val graphics = splitAll(arr, Array[Graphic](), RegularBullet)
      val finalGraphic = handleTexts(graphics)
      val g = bullet beside finalGraphic
      val newBounds = RectBounds(g.bounds.left, g.bounds.right, g.bounds.top - 10, g.bounds.bottom)
      Bounded(g, newBounds)
    case Regular =>
      val arr = text.split(" ")
      val graphics = splitAll(arr, Array[Graphic](), Regular)
      handleTexts(graphics)
    case Custom(_, _, _) =>
      val arr = text.split(" ")
      val graphics = splitAll(arr, Array[Graphic](), font)
      handleTexts(graphics)
  }

  // reads file
  def readLines(filename: String): List[String] = {
  	//todo
  }

  val slideLines = readLines("scales-ide.txt") //todo
  val currentGraphic: Var[Graphic] = Var[Text("")] //todo

  //gets the right line from slideLines
  def fnSlideClick(x: Int): Graphic = {
  	// todo
  }
}
