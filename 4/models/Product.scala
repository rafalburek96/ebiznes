package models
 
import play.api.libs.json.Json

case class Product(
  id: Long,
  productName: String,
  productDescription: String,
  productPrice: Int,
  category: Long)

object Product {
  implicit val productFormat = Json.format[Product]
}
