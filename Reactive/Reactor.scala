import scalatags.JsDom.all._
import rx._

case class Graphic {} //just placeholder for actual Graphic

/*
** Usage example: Reactor(Reactive.ClockTick, ...)  
** The above creates a reactor that reacts to clock ticks
*/
object Reactive {
	type Reactive = Int

	final val ClockTick: Reactive = 0
	final val MouseClick: Reactive = 1
	final val MousePosition: Reactive = 2

	final val MouseClickX: Reactive = 3
	final val MouseClickY: Reactive = 4
	final val MousePositionX: Reactive = 5
	final val MousePositionY: Reactive = 6

	final val ClockTickGetMousePosition: Reactive = 7
	final val MouseClickGetClockTime: Reactive = 8
	final val MousePositionGetClockTime: Reactive = 9
}

/*
** Usage:
** @params reaction: a final val from Reactive, such as Reactive.MouseClickGetClockTime
*/
import Reactive._
case class Reactor[T](reaction: Reactive, fn: T => Graphic, framesPerSecond: Int = 0, duration: Double = 0) {
	val target: Rx[Any] = reaction match {
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
		case Reactive.MouseClickGetClockTime => {
			val click_sub = MouseClick.subscribe
			val startTime = new js.Date().getTime()
			val rx = Var(0.0)
			Obs(click_sub) {
				rx() = (new js.Date().getTime() - startTime) / 1000
			}
			rx
		}
		case Reactive.MousePositionGetClockTime => {
			val pos_sub = MousePosition.subscribe
			val startTime = new js.Date().getTime()
			val rx = Var(0.0)
			Obs(pos_sub) {
				rx() = (new js.Date().getTime() - startTime) / 1000
			}
			rx
		}
	}

	Obs(target) {
		val graphic = fn(target())
		//todo: now render the graphic
	}
}