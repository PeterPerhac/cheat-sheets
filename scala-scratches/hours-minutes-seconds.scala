val R = """.*(\d\d?):(\d\d?):(\d\d?).*""".r
case class HMS(h:Int, m: Int, s:Int);
val total = scala.io.Source.fromFile("./data/hms.txt").getLines.collect {
  case R(hh,mm,ss) => HMS(hh.toInt, mm.toInt, ss.toInt) 
}.foldLeft(0){(acc, hms) => acc+hms.s+hms.m*60+hms.h*3600}
def normaliseTotal(s:Int):HMS = {
  val h = s / 3600
  val m = (s - h*3600) / 60
  HMS(h, m, s - h*3600 - m*60)
}
println(normaliseTotal(total))
