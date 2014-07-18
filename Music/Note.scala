import scalatags.JsDom.all._
import rx._ 

object Audio {
	def getAudioContext(): js.Dynamic = js.Dynamic.newInstance(js.Dynamic.global.AudioContext)
}

case class Note(val freq: Double = 0, val vol: Double = 1) {

}

//'s' after note indicates sharp
object C extends Note(Audio.getAudioContext(), 16.35)
object Cs extends Note(Audio.getAudioContext(), 17.32)
object D extends Note(Audio.getAudioContext(), 18.35)
object Ds extends Note(Audio.getAudioContext(), 19.45)
object E extends Note(Audio.getAudioContext(), 20.60)
object F extends Note(Audio.getAudioContext(), 21.83)
object Fs extends Note(Audio.getAudioContext(), 23.12)
object G extends Note(Audio.getAudioContext(), 24.50)
object Gs extends Note(Audio.getAudioContext(), 25.96)
object A extends Note(Audio.getAudioContext(), 27.50)
object As extends Note(Audio.getAudioContext(), 29.14)
object B extends Note(Audio.getAudioContext(), 30.87)