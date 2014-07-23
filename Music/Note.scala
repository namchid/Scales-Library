package edu.depauw.scales.music

import scala.scalajs.js
import js.annotation.JSExport
import org.scalajs.dom
import rx._

trait Scales {}

sealed trait Audio {}

case class ExpRamp(val targetFreq: Double) extends Audio
case class LinRamp(targetFreq: Double) extends Audio
case class Rampless() extends Audio
case class XBeats(val times: Int, val beatDuration: Double, val beatPause: Double) extends Audio

object Audio {
	val audioContext: js.Dynamic = js.Dynamic.newInstance(js.Dynamic.global.AudioContext)()
	val times = Var(0)

	def ExponentialRamp(targetFreq: Double) = ExpRamp(targetFreq)
	def LinearRamp(targetFreq: Double) = LinRamp(targetFreq)
	def NoRamp() = Rampless()
	def Beats(times: Int, beatDuration: Double, beatPause: Double) = XBeats(times, beatDuration, beatPause)
}

sealed case class Sound(freq: Double = 0, vol: Double = 1) {
	val ctx = Audio.audioContext

	def currentTime(): Double = {
		val time = ctx.currentTime.toString()
		return time.toDouble
	}

	def play(time: Double, dur: Double): Unit = {
		val oscillatorNode = ctx.createOscillator()
		val gainNode = ctx.createGain()

		oscillatorNode.frequency.value = freq
		gainNode.gain.value = vol

		oscillatorNode.connect(gainNode)
		gainNode.connect(ctx.destination)

		oscillatorNode.start(time)
		oscillatorNode.stop(ctx.currentTime + dur)
	}

	def playRamp(start: Double = 0, duration: Double = 1, targetFreq: Double = 0, rampOption: String = "linear"): Unit = {
		val oscillatorNode = ctx.createOscillator()
		val gainNode = ctx.createGain()

		oscillatorNode.frequency.value = freq
		val startTime = currentTime() + start
		gainNode.gain.setValueAtTime(vol, startTime)

		rampOption match {
			case "linear" => 
				gainNode.gain.linearRampToValueAtTime(targetFreq, startTime + duration)
			case "exponential" => 
				if(targetFreq > freq) 
					gainNode.gain.exponentialRampToValueAtTime(targetFreq, startTime + duration)
		}

		oscillatorNode.start(startTime)
		oscillatorNode.stop(startTime + duration)

		oscillatorNode.connect(gainNode)
		gainNode.connect(ctx.destination)
	}

	def playBeats(start: Double = 0, times: Int = 1, beatDuration: Double = .5, beatPause: Double = .5): Unit = {
		// Audio.times() += 1
		// if(Audio.times() < 5) {
		// 	initContext
		// 	Audio.times() = 0
		// }
		playB(beatDuration, beatPause, 0, times, currentTime() + start)
	}

	private def playB(beatDuration: Double, beatPause: Double, timesPlayed: Int, xTimes: Int, currTime: Double): Unit = {
		(timesPlayed == xTimes) match {
			case true =>
			case false =>
				play(currTime, currTime + beatDuration)
				playB(beatDuration, beatPause, timesPlayed + 1, xTimes, currTime + beatDuration + beatPause)
		}
	}

}

case class Note(option: Audio, freq: Double = 170, start: Double = 0, vol: Double = 1, duration: Double = 1) extends Scales {
	val note = Sound(freq, vol)
	
	option match {
		// case x: Rampless =>
		// 	note.play(start, x.duration)
		// case x: LinRamp =>
		// 	note.playRamp(start, x.duration, x.targetFreq, "linear")
		// case x: ExpRamp =>
		// 	note.playRamp(start, x.duration, x.targetFreq, "exponential")
		// case x: XBeats =>
		// 	note.playBeats(start, x.times, x.beatDuration, x.beatPause)
		// case _ =>
		case x: Rampless =>
			note.play(start, duration)
		case x: LinRamp =>
			note.playRamp(start, duration, x.targetFreq, "linear")
		case x: ExpRamp =>
			note.playRamp(start, duration, x.targetFreq, "exponential")
		case x: XBeats =>
			note.playBeats(start, x.times, x.beatDuration, x.beatPause)
		case _ =>
	}

	def >(octaves: Int = 1): Note = {
		val mult = math.pow(2, octaves)
		Note(option, freq * mult, start, vol)
	}

	def <(octaves: Int = 1): Note = {
		val mult = math.pow(2, octaves)
		Note(option, freq / mult, start, vol)
	}

	def >>>> : Note = {
		Note(option, freq * 16, start, vol)
	}

	def <<<< : Note = {
		Note(option, freq / 16, start, vol)
	}

	def setVolume(newVolume: Double): Note = {
		Note(option, freq, start, newVolume)
	}

	def louder(other: Note): Boolean = {
		this.vol > other.vol
	}

	def higherFrequency(other: Note): Boolean = {
		this.freq > other.freq
	}

	def frequency: Double = freq

	def volume: Double = vol
}

case class NoteSeq(notes: Note*) extends Scales {
	// for(n <- notes) {
	// 	val note = Sound(n.freq, n.vol)
	// 	n.option match {
	// 		case x: Rampless =>
	// 			note.play(n.start, x.duration)
	// 		case x: LinRamp =>
	// 			note.playRamp(n.start, x.duration, x.targetFreq, "linear")
	// 		case x: ExpRamp =>
	// 			note.playRamp(n.start, x.duration, x.targetFreq, "exponential")
	// 		case x: XBeats =>
	// 			note.playBeats(n.start, x.times, x.beatDuration, x.beatPause)
	// 	}
	// }
}

object C extends Note(Audio.NoRamp, 16.35)
object Cs extends Note(Audio.NoRamp, 17.32)
object D extends Note(Audio.NoRamp, 18.35)
object Ds extends Note(Audio.NoRamp, 19.45)
object E extends Note(Audio.NoRamp, 20.60)
object F extends Note(Audio.NoRamp, 21.83)
object Fs extends Note(Audio.NoRamp, 23.12)
object G extends Note(Audio.NoRamp, 24.50)
object Gs extends Note(Audio.NoRamp, 25.96)
object A extends Note(Audio.NoRamp, 27.50)
object As extends Note(Audio.NoRamp, 29.14)
object B extends Note(Audio.NoRamp, 30.87)