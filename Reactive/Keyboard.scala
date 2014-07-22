package edu.depauw.scales.reactive

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom
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

	def subscribe(): Rx[Int] = keyPressedCode
}

/*
** Usage example: KeyPressed(Key.Enter).subscribe()
*/
object Key {
	type KeyType = Int

	final val Backspace: KeyType = 8
	final val Tab: KeyType = 9
	final val Enter: KeyType = 13
	final val Shift: KeyType = 16
	final val Ctrl: KeyType = 17
	final val Alt: KeyType = 18
	final val Space: KeyType = 32

	final val Left: KeyType = 37
	final val Up: KeyType = 38
	final val Right: KeyType = 39
	final val Down: KeyType = 40

	final val Zero: KeyType = 48
	final val One: KeyType = 49
	final val Two: KeyType = 50
	final val Three: KeyType = 51
	final val Four: KeyType = 52
	final val Five: KeyType = 53
	final val Six: KeyType = 54
	final val Seven: KeyType = 55
	final val Eight: KeyType = 56
	final val Nine: KeyType = 57

	final val A: KeyType = 65
	final val B: KeyType = 66
	final val C: KeyType = 67
	final val D: KeyType = 68
	final val E: KeyType = 69
	final val F: KeyType = 70
	final val G: KeyType = 71
	final val H: KeyType = 72
	final val I: KeyType = 73
	final val J: KeyType = 74
	final val K: KeyType = 75
	final val L: KeyType = 76
	final val M: KeyType = 77
	final val N: KeyType = 78
	final val O: KeyType = 79
	final val P: KeyType = 80
	final val Q: KeyType = 81
	final val R: KeyType = 82
	final val S: KeyType = 83
	final val T: KeyType = 84
	final val U: KeyType = 85
	final val V: KeyType = 86
	final val W: KeyType = 87
	final val X: KeyType = 88
	final val Y: KeyType = 89
	final val Z: KeyType = 90
}

//note: not tested on numpad numbers/keys. these probably need wrapping
import Key._
case class KeyPress(key: Key.KeyType) {	
	val keyboard = Keyboard.subscribe
	val timesPressed = Var(0)

	Obs(keyboard) {
		(keyboard() == key) match {
			case true => timesPressed() += 1
			case false =>
		}
	}

	def subscribe(): Rx[Int] = timesPressed
}