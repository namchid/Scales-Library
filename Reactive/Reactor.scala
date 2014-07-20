import scalatags.JsDom.all._
import rx._

case class Graphic {} //just placeholder for actual Graphic

object Canvas {} //just a placeholder for actual Canvas

/*
** Usage example: Reactor(Reactive.ClockTick, ...)  
** The above creates a reactor that reacts to clock ticks
*/
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

/*
** Usage:
** @params reaction: a final val from Reactive, such as Reactive.MouseClickGetClockTime
*/
case class Reactor[T](reaction: Reactive.ReactionType, fn: T => Graphic, fillStyle: String = "grey", strokeStyle: String = "black", lineWidth: Int = 1, 
	framesPerSecond: Double = 0, duration: Double = 0) {
	
	val function = fn.asInstanceOf[(Any => Graphic)]

	val target: Rx[Any] = reaction match {
		case Reactive.ClockTick => 
			Timer(framesPerSecond, duration).subscribe

		case Reactive.MouseClick => 
			MouseClick.subscribe
		
		case Reactive.MousePosition => 
			MouseClick.subscribe
		
		case Reactive.MouseClickX => 
			MouseClick.subscribeX
		
		case Reactive.MouseClickY => 
			MouseClick.subscribeY
		
		case Reactive.MousePositionX => 
			MousePosition.subscribeX
		
		case Reactive.MousePositionY => 
			MousePosition.subscribeY
		
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

		case _ => Var(0) // default value never changes
	}

	val index = Var(-1)

	private def initGraphic(g: Graphic): Unit = {
		CanvasHandler.setAll(index(), fillStyle, strokeStyle, lineWidth)
		CanvasHandler.addGraphic(g)
	}

	val changes = Var(0)
	def countChanges(): Int = changes()
	def unsubscribe(): Unit = {
		target().killAll()
	}

	Obs(target) {
		val g = function(target())
		changes() += 1
		if(index() == -1) {
			index() = CanvasHandler.getIndex
			initGraphic(g)
		} else {
			CanvasHandler.updateGraphic(index(), g)
		}
	}
}