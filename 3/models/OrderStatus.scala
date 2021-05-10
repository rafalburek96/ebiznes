package models
 
import play.api.libs.json.Json

case class OrderStatus(
  id: Long,
  orderStatusCode: Int,
  orderStatusDescription: String)

object OrderStatus {
  implicit val orderStatusFormat = Json.format[OrderStatus]
}
