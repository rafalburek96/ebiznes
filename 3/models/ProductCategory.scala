package models
 
import play.api.libs.json.Json

case class ProductCategory(
  id: Long,
  categoryName: String,
  categoryDescription: String)

object ProductCategory {
  implicit val productCategoryFormat = Json.format[ProductCategory]
}
