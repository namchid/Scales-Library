import scalatags.JsDom.all._
import rx._

/*
** Usage: Timer(fps, dur).subscribe()
** This returns a rx double, the updated time, based on inputs
*/
case class Timer(framesPerSecond: Int, duration: Double) {
	val totalFrames = duration * framesPerSecond
	val frameSpeed = (duration * 1000) / totalFrames
	val currentTime = Var(0.0)

	val timeElapsed = Rx {currentTime() - startTime}
	val currFrame = Rx {(timeElapsed() / frameSpeed).toInt}
	val frameBasedTime = Rx{(currFrame() * frameSpeed) / 1000}

	private def listen() {
		currentTime() = new js.Date().getTime()
		if(currFrame() >= totalFrames) {}
		else {
			val timeError = timeElapsed() - (frameBasedTime() * 1000)
			dom.setTimeout(() => listen(), (frameSpeed - timeError))
		}
	}

	val startTime = new js.Date().getTime()
	dom.setTimeout(() => listen(), frameSpeed)

	def subscribe(): Rx[Double] = frameBasedTime
}