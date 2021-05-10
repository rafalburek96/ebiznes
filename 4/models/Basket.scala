package models
 
import play.api.libs.json.Json

case class Basket(id: Long,
                  customerID: Long,
                  basketDateAdded: String,
                  isBasketCompleted: Boolean,
                  )

object Basket {
  implicit val basketFormat = Json.format[Basket]
  implicit val writeBasket = Json.writes[Basket]
}
