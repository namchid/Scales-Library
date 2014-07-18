import scalatags.JsDom.all._
import rx._

case class Graphic {} //just placeholder for actual Graphic

/*
** Usage example: Reactor(Reactive.ClockTick, ...)  
** The above creates a reactor that reacts to clock ticks
*/
object Reactive {
	final val ClockTick = 0
	final val MouseClick = 1
	final val MousePosition = 2

	final val MouseClickX = 3
	final val MouseClickY = 4
	final val MousePositionX = 5
	final val MousePositionY = 6

	final val ClockTickGetMousePosition = 7
	final val MouseClickGetClockTick = 8
}

/*
** Usage:
** @params reaction: a final val from Reactive, such as Reactive.MouseClickGetClockTick
*/
case class Reactor[T](reaction: Int, fn: T => Graphic, framesPerSecond: Int = 0, duration: Double = 0) {
	val observe = reaction match {
		case Reactive.ClockTick = Timer.subscribe
		case Reactive.MouseClick = MouseClick.subscribe
		case Reactive.MousePosition = MouseClick.subscribe
		case Reactive.MouseClickX = 
	}
}