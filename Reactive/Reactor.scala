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
	val observe: Rx[Any] = reaction match {
		case Reactive.ClockTick => Timer(framesPerSecond, duration).subscribe
		case Reactive.MouseClick => MouseClick.subscribe
		case Reactive.MousePosition => MouseClick.subscribe
		case Reactive.MouseClickX => {
			val sub = MouseClick.subscribe
			Rx {sub()._1}
		}
		case Reactive.MouseClickY => {
			val sub = MouseClick.subscribe
			Rx {sub()._2}
		}
		case Reactive.MousePositionX => {
			val sub = MousePosition.subscribe
			Rx {sub()._1}
		}
		case Reactive.MousePositionY => {
			val sub = MousePosition.subscribe
			Rx {sub()._2}
		}
		case Reactive.ClockTickGetMousePosition => {
			val clock_sub = Timer(framesPerSecond, duration).subscribe
			val rx = Var(MouseMove.xy())
			Obs(clock_sub) {
				rx() = MouseMove.xy()
			}
			rx
		}
		case Reactive.MouseClickGetClockTick => {
			val click_sub = MouseClick.subscribe
			val startTime = new js.Date().getTime()
			val rx = Var(0.0)
			Obs(click_sub) {
				rx() = (new js.Date().getTime() - startTime)/1000 //returns time in seconds
			}
			rx
		}
	}
	//todo: based on whatever reactive variable observe is, call fn and pass the appropriate arguments
}