import scalatags.JsDom.all._
import rx._

case class Graphic {} //just placeholder for actual Graphic

object Canvas {} //just a placeholder for actual Canvas

/*
** Usage example: Not called directly by user
*/
sealed trait Reactive {}

case class CTick(val fps: Double, val dur: Double) extends Reactive
case class KPress(val key: Key.KeyType) extends Reactive
case class MClick() extends Reactive
case class MPos() extends Reactive


case class MClickX extends Reactive
case class MClickY extends Reactive
case class MPosX extends Reactive
case class MPosY extends Reactive

case class CTickGetMPos(val fps: Double, val dur: Double) extends Reactive
case class MClickGetCTime() extends Reactive
case class MPosGetCTime() extends Reactive

/*
** Usage example: Reactor(Reactive.ClockTick(Double, Double), ...)  
** The example above creates a reactor that reacts to clock ticks
*/
object Reactive {
	def ClockTick(framesPerSecond: Double, duration: Double) = CTick(framesPerSecond, duration)
	def KeyPress(key: Key.KeyType) = KPress(key)
	def MouseClick() = MClick()
	def MousePosition() = MPos()

	def MouseClickX() = MClickX()
	def MouseClickY() = MClickY()
	def MousePositionX() = MPosX()
	def MousePositionY() = MPosY()

	def ClockTickGetMousePosition(framesPerSecond: Double, duration: Double) = CTickGetMPos(framesPerSecond, duration)
	def MouseClickGetClockTime() = MClickGetCTime()
	def MousePositionGetClockTime() = MPosGetCTime() 
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