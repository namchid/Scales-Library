package edu.depauw.scales.reactive

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom
import rx._

import edu.depauw.scales.graphics._

/*
** Usage example: Not called directly by user
*/
sealed trait Reactive {}

case class CTick(val fps: Double, val dur: Double) extends Reactive
case class KPress(val key: Key.KeyType) extends Reactive
case class KPressAny() extends Reactive
case class MClick() extends Reactive
case class MPos() extends Reactive

case class MClickX() extends Reactive
case class MClickY() extends Reactive
case class MPosX() extends Reactive
case class MPosY() extends Reactive

case class CTickGetMPos(val fps: Double, val dur: Double) extends Reactive
case class MClickGetCTime() extends Reactive
case class MPosGetCTime() extends Reactive
case class KPressGetCTime(val key: Key.KeyType) extends Reactive
case class KPressAnyGetCTime() extends Reactive

/*
** Usage example: Reactor(Reactive.ClockTick(Double, Double), ...)  
** The example above creates a reactor that reacts to clock ticks
*/
object Reactive {
	def ClockTick(framesPerSecond: Double, duration: Double) = CTick(framesPerSecond, duration)
	def KeyPress(key: Key.KeyType) = KPress(key)
	def KeyPress() = KPressAny()
	def MouseClick() = MClick()
	def MousePosition() = MPos()

	def MouseClickX() = MClickX()
	def MouseClickY() = MClickY()
	def MousePositionX() = MPosX()
	def MousePositionY() = MPosY()

	def ClockTickGetMousePosition(framesPerSecond: Double, duration: Double) = CTickGetMPos(framesPerSecond, duration)
	def MouseClickGetClockTime() = MClickGetCTime()
	def MousePositionGetClockTime() = MPosGetCTime()
	def KeyPressGetClockTime(key: Key.KeyType) = KPressGetCTime(key)
	def KeyPressGetClockTime() = KPressAnyGetCTime() 
}

/*
** Usage example: Reactor(Reactive.ClockTick(2, 10), fn, "red", "blue", 0)
** @params reaction: a final val from Reactive, such as Reactive.MouseClickGetClockTime
*/
case class Reactor[T](reaction: Reactive, fn: T => Graphic, fillStyle: String = "grey", strokeStyle: String = "black", lineWidth: Int = 1) {
	
	val function = fn.asInstanceOf[(Any => Graphic)]

	val target: Rx[Any] = reaction match {
		case x: CTick => 
			Timer(x.fps, x.dur).subscribe

		case x: KPress =>
			KeyPress(x.key).subscribe

		case x: KPressAny =>
			Keyboard.subscribe

		case x: MClick => 
			MouseClick.subscribe
		
		case x: MPos => 
			MouseClick.subscribe
		
		case x: MClickX => 
			MouseClick.subscribeX
		
		case x: MClickY => 
			MouseClick.subscribeY
		
		case x: MPosX => 
			MousePosition.subscribeX
		
		case x: MPosY => 
			MousePosition.subscribeY
		
		case x: CTickGetMPos => {
			val clock_sub = Timer(x.fps, x.dur).subscribe
			val rx = Var(MousePosition.xy())
			Obs(clock_sub) {
				rx() = MousePosition.xy()
			}
			rx
		}
		
		case x: MClickGetCTime => {
			val click_sub = MouseClick.subscribe
			val startTime = new js.Date().getTime()
			val rx = Var(0.0)
			Obs(click_sub) {
				rx() = (new js.Date().getTime() - startTime) / 1000
			}
			rx
		}
		
		case x: MPosGetCTime => {
			val pos_sub = MousePosition.subscribe
			val startTime = new js.Date().getTime()
			val rx = Var(0.0)
			Obs(pos_sub) {
				rx() = (new js.Date().getTime() - startTime) / 1000
			}
			rx
		}

		case x: KPressGetCTime => {
			val key_sub = KeyPress(x.key).subscribe
			val startTime = new js.Date().getTime()
			val rx = Var(0.0)
			Obs(key_sub) {
				rx() = (new js.Date().getTime() - startTime) / 1000
			}
			rx
		}

		case x: KPressAnyGetCTime => {
			val key_sub = Keyboard.subscribe
			val startTime = new js.Date().getTime()
			val rx = Var(0.0)
			Obs(key_sub) {
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
		target.killAll()
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