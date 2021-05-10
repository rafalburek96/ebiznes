package models
 
import play.api.libs.json.{ Json }

case class Address(id: Long = 0, street: String, flatNumber: Int, zipCode: String)

object Address {
  implicit val addressFormat = Json.format[Address]
}
