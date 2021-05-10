package models
 
import play.api.libs.json.Json

case class Order(
  id: Long,
  customerID: Long,
  basketID: Long,
  deliveryDate: String,
  purchaseDate: String,
  orderStatusCode: Int)

object Order {
  implicit val orderFormat = Json.format[Order]
}
