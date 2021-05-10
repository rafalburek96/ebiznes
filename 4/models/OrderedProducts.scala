package models
 
import play.api.libs.json.Json

case class OrderedProducts(
  id: Long,
  productID: Long,
  productName: String,
  quantity: Int,
  orderID: Long)

object OrderedProducts {
  implicit val orderedProductsFormat = Json.format[OrderedProducts]
}
