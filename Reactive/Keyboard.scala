import scalatags.JsDom.all._
import rx._

object Keyboard {
	val keyPressed = Var(' ')

	private def listen() = Rx {
		dom.onkeydown = {
			(e: dom.KeyboardEvent) => 
				keyPressed() = js.Dynamic.global.String.fromCharCode(e.keyCode).toString.toLowerCase.head.toChar
		}
	}

	listen()
	def subscribe(): Rx[Char] = keyPressed
}

//currently only works for [a-z0-9]. todo: add options for spacebar, nums, etc.
case class KeyPressed(key: Char) {
	val k = key.toString.toLowerCase.head.toChar
	val keyboard = Keyboard.subscribe
	val timesPressed = Var(0)
	Obs(keyboard) {
		(keyboard() == k) match {
			case true => timesPressed() += 1
			case false =>
		}
	}

	def subscribe(): Rx[Int] = timesPressed
}