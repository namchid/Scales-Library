import scalatags.JsDom.all._
import rx._ 

object Audio {
	def getAudioContext(): js.Dynamic = js.Dynamic.newInstance(js.Dynamic.global.AudioContext)

	type AudioRampOption: String
	final val Ramp: AudioRampOption = "ramp"
	final val LinearRamp: AudioRampOption = "linear"
	final val NoRamp: AudioRampOption = "none"
}

case class Note(val freq: Double = 0, val vol: Double = 1) {
	val ctx = Audio.getAudioContext()
	val currentNode: Var[js.Dynamic] = Var(null)

	def currentTime(): Double = {
		val time = ctx.currentTime.toString()
		return time.toDouble
	}

	def play(time: Double = 0): Unit = {
		//todo
	}

	def stop(delay: Double = 0): Unit = {
		//todo
	}

	import Audio._
	def playRamp(start: Double, duration: Double, targetFrequency: Double = 0, rampOption: AudioRampOption = Audio.NoRamp): Unit = {
		//todo
	}

	def playBeats(start: Double = 0, times: Int = 1, beatDuration: Double = .2, beatPause: Double = .2): Unit = {
		//todo
	}

	private def playBeatsHelper(beatDuration: Double, beatPause: Double, timesPlayed: Int, totalTimes: Int, currTime: Double): Unit = {
		//todo
	}

	def >(octaves: Int = 1): Note = {
		//todo
	}

	def <(octaves: Int = 1): Note = {

	}

	def >>>>: Note = {
		//todo
	}

	def <<<<: Note = {
		//todo
	}

	def setVolume(vol: Double): Note = {
		//todo
	}

	def louder(other: Note): Boolean = {
		//todo
	}

	def higherFrequency(other: Note): Boolean = {
		//todo
	}

	def frequency: Double = freq

	def volume: Double = vol
}

//'s' after note indicates sharp
object C extends Note(16.35)
object Cs extends Note(17.32)
object D extends Note(18.35)
object Ds extends Note(19.45)
object E extends Note(20.60)
object F extends Note(21.83)
object Fs extends Note(23.12)
object G extends Note(24.50)
object Gs extends Note(25.96)
object A extends Note(27.50)
object As extends Note(29.14)
object B extends Note(30.87)