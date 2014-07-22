import scalatags.JsDom.all._
import rx._ 

object Audio {
	def getAudioContext(): js.Dynamic = js.Dynamic.newInstance(js.Dynamic.global.AudioContext)

	type AudioRampOption: String
	final val ExponentialRamp: AudioRampOption = "ramp"
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
		val oscillatorNode = ctx.createOscillator()
		val gainNode = ctx.createGain()

		oscillatorNode.frequency.value = freq
		gainNode.gain.value = vol

		oscillatorNode.connect(gainNode)
		gainNode.connect(ctx.destination)
	}

	def stop(delay: Double = 0): Unit = {
		if(currentNode() != null) {
			currentNode().stop(ctx.currentTime + dur)
			currentNode() = null
		}
	}

	import Audio._
	def playRamp(start: Double, duration: Double, targetFrequency: Double = 0, rampOption: AudioRampOption = Audio.NoRamp): Unit = {
		val oscillatorNode = ctx.createOscillator()
		val gainNode = ctx.createGain()

		oscillatorNode.frequency.value = freq
		gainNode.gain.value = vol

		rampOption match {
			case Audio.NoRamp =>
			case Audio.LinearRamp =>
				gainNode.gain.linearRampToValueAtTime(targetFrequency, start + duration)
			case Audio.ExponentialRamp =>
				if(targetFrequency > freq) gainNode.gain.exponentialRampToValueAtTime(target, start + dur)
		}

		oscillatorNode.connect(gainNode)
		gainNode.connect(ctx.destination)

		oscillatorNode.start(start)
		oscillatorNode.stop(start + dur)
	}

	def playBeats(start: Double = 0, times: Int = 1, beatDuration: Double = .2, beatPause: Double = .2): Unit = {
		//todo
	}

	private def playBeatsHelper(beatDuration: Double, beatPause: Double, timesPlayed: Int, totalTimes: Int, currTime: Double): Unit = {
		//todo
	}

	def >(octaves: Int = 1): Note = {
		val mult = math.pow(2, octaves)
		Note(freq * mult, vol)
	}

	def <(octaves: Int = 1): Note = {
		val mult = math.pow(2, octaves)
		Note(freq / mult, vol)
	}

	def >>>>: Note = {
		Note(freq * 16, vol)
	}

	def <<<<: Note = {
		Note(freq / 16, vol)
	}

	def setVolume(newVolume: Double): Note = {
		Note(freq, newVolume)
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