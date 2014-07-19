import scalatags.JsDom.all._
import rx._

/*
** Usage: Keyboard.subscribe()
** This returns a rx int, the keycode of the key pressed
*/
object Keyboard {
	val keyPressedCode = Var(0)

	private def listen() = Rx {
		dom.onkeydown = { 
			(e: dom.KeyboardEvent) => keyPressedCode() = e.keyCode
		}
	}

	listen()

	def subscribe(): Rx[Char] = keyPressedCode
}

/*
** Usage example: KeyPressed(Key.Enter).subscribe()
*/
object Key {
	type Key = Int

	final val Backspace: Key = 8
	final val Tab: Key = 9
	final val Enter: Key = 13
	final val Shift: Key = 16
	final val Ctrl: Key = 17
	final val Alt: Key = 18

	final val Left: Key = 37
	final val Up: Key = 38
	final val Right: Key = 39
	final val Down: Key = 40

	final val Zero: Key = 48
	final val One: Key = 49
	final val Two: Key = 50
	final val Three: Key = 51
	final val Four: Key = 52
	final val Five: Key = 53
	final val Six: Key = 54
	final val Seven: Key = 55
	final val Eight: Key = 56
	final val Nine: Key = 57

	final val A: Key = 65
	final val B: Key = 66
	final val C: Key = 67
	final val D: Key = 68
	final val E: Key = 69
	final val F: Key = 70
	final val G: Key = 71
	final val H: Key = 72
	final val I: Key = 73
	final val J: Key = 74
	final val K: Key = 75
	final val L: Key = 76
	final val M: Key = 77
	final val N: Key = 78
	final val O: Key = 79
	final val P: Key = 80
	final val Q: Key = 81
	final val R: Key = 82
	final val S: Key = 83
	final val T: Key = 84
	final val U: Key = 85
	final val V: Key = 86
	final val W: Key = 87
	final val X: Key = 88
	final val Y: Key = 89
	final val Z: Key = 90
}

//note: not tested on numpad numbers/keys. these probably need wrapping
import Key._
case class KeyPressed(key: Key) {	
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