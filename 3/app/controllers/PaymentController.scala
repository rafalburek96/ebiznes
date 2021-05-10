package controllers
 
import javax.inject._
import models.{ OrderRepository, Payment, PaymentRepository }
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.Forms.mapping
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

case class CreatePaymentForm(isPaymentFinished: Boolean, orderID: Long)
case class UpdatePaymentForm(id: Long, isPaymentFinished: Boolean, orderID: Long)

@Singleton
class PaymentController @Inject() (paymentRepository: PaymentRepository, orderRepository: OrderRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val paymentForm: Form[CreatePaymentForm] = Form {
    mapping(
      "isPaymentFinished" -> boolean,
      "orderID" -> longNumber)(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  val updatedPaymentForm: Form[UpdatePaymentForm] = Form {
    mapping(
      "id" -> longNumber,
      "isPaymentFinished" -> boolean,
      "orderID" -> longNumber)(UpdatePaymentForm.apply)(UpdatePaymentForm.unapply)
  }

  // -> JSON
  def createPayment: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val payment = json.as[Payment]
    val result = paymentRepository.create(payment.isPaymentFinished, payment.orderID)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getPayments: Action[AnyContent] = Action.async {
    val list = paymentRepository.list()
    list map { r =>
      Ok(Json.toJson(r))
    }
  }

  def removePayment(id: Long): Action[AnyContent] = Action { implicit request =>
    paymentRepository.delete(id)
    Redirect(routes.PaymentController.getPayments())
  }

  def updatePayment(id: Long): Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val p = json.as[Payment]
    val updated = paymentRepository.update(p.id, p)
    updated map {
      r => Ok(Json.toJson(p))
    }
  }

  // -> FORM
  def createPaymentForm(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.createPayment(paymentForm))
  }

  def createPaymentHandler() = Action.async { implicit request =>
    paymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.createPayment(errorForm)))
      },
      payment => {
        paymentRepository.create(payment.isPaymentFinished, payment.orderID).map { _ =>
          Ok("Success")
        }
      })
  }

  def getPaymentForm(): Action[AnyContent] = Action.async { implicit request =>
    val list = paymentRepository.list()
    list.map(payment => Ok(views.html.payments(payment)))
  }

  def updatePaymentFrom(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val payment = paymentRepository.getByID(id)
    payment.map(o => {
      val form = updatedPaymentForm.fill(UpdatePaymentForm(o.id, o.isPaymentFinished, o.orderID))
      Ok(views.html.paymentUpdate(form))
    })
  }

  def updatePaymentHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updatedPaymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.paymentUpdate(errorForm)))
      },
      payment => {
        paymentRepository.update(payment.id, Payment(payment.id, payment.isPaymentFinished, payment.orderID)).map { _ =>
          Redirect(routes.PaymentController.updatePayment(payment.id)).flashing("success" -> "Payment updated")
        }
      })
  }
}
