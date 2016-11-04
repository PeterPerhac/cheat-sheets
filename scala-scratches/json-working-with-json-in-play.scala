import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

val json: JsValue = Json.parse("""
{
  "name" : "Watership Down",
  "location" : {
    "lat" : 51.235685,
    "long" : -1.309197
  },
  "residents" : [ {
    "name" : "Fiver",
    "age" : 4,
    "role" : null
  }, {
    "name" : "Bigwig",
    "age" : 6,
    "role" : "Owsla"
  } ]
}
""")

case class Location(lat: Double, long: Double)
case class Resident(name: String, age: Int, role: Option[String])
case class Place(name: String, location: Location, residents: Seq[Resident])

implicit val residentFormat = Json.format[Resident]
implicit val locationFormat = Json.format[Location]
implicit val theThingFormat = Json.format[Place]

val place = json.as[Place]

