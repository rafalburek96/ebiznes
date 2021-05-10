package models
 
import play.api.libs.json.Json

case class Review(
  id: Long,
  reviewRating: Int,
  productID: Long,
  customerID: Long)

object Review {
  implicit val reviewFormat = Json.format[Review]
}
