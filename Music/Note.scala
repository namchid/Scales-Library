import scalatags.JsDom.all._
import rx._ 

object Audio {
	def getAudioContext(): js.Dynamic = js.Dynamic.newInstance(js.Dynamic.global.AudioContext)
}

case class Note(val freq: Double = 0, val vol: Double = 1) {

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