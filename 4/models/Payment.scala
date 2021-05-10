package models
 
import play.api.libs.json.Json

case class Payment(
  id: Long,
  isPaymentFinished: Boolean,
  orderID: Long)

object Payment {
  implicit val paymentFormat = Json.format[Payment]
}
