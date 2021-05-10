package models
 
import play.api.libs.json.Json

case class Customer(
  id: Long,
  name: String,
  surname: String,
  email: String,
  phoneNumber: String,
  password: String,
  address: Long)

object Customer {
  implicit val customerFormat = Json.format[Customer]
}
