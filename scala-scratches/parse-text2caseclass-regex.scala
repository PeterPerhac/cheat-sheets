val R = """\|....\|.(\d\d\d\d-\d\d-\d\d)[^\d]+(\d+)[^\d]+([\d.]+)[^\d]+([\d.]+)[^\d]+([\d.]+).*$""".r
case class Reading(readingDate:String, mileage:Int, miles:Double, litres:Double, cost:Double );
val readings = scala.io.Source.fromFile("/home/pperhac/temp/refuel.txt").getLines.map(l => l match {case R(d,tot,mi,l,c) => Reading(d ,tot.toInt, mi.toDouble, l.toDouble, c.toDouble) }).toList
val newVals = readings.drop(1).scanLeft(readings.head.mileage.toDouble) {(acc,e) => acc + e.miles}

