package controllers
 
import javax.inject._
import models.{OrderStatus, OrderStatusRepository}
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class CreateStatusForm(orderStatusCode: Int, orderStatusDescription: String)
case class UpdateStatusForm(id: Long,orderStatusCode: Int, orderStatusDescription: String)

@Singleton
class OrderStatusController @Inject()(orderStatusRepository: OrderStatusRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val statusForm: Form[CreateStatusForm] = Form {
    mapping(
      "orderStatusCode" -> number,
      "orderStatusDescription" -> nonEmptyText,
    )(CreateStatusForm.apply)(CreateStatusForm.unapply)
  }

  val updatedStatusForm: Form[UpdateStatusForm] = Form {
    mapping(
      "id" -> longNumber,
      "orderStatusCode" -> number,
      "orderStatusDescription" -> nonEmptyText,
    )(UpdateStatusForm.apply)(UpdateStatusForm.unapply)
  }

  // -> JSON
  def createStatus: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val status = json.as[OrderStatus]
    val result = orderStatusRepository.create(status.orderStatusCode, status.orderStatusDescription)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getStatuses: Action[AnyContent] = Action.async {
    val list = orderStatusRepository.list()
    list map { r =>
      Ok(Json.toJson(r))
    }
  }

  def removeStatus(id: Long): Action[AnyContent] = Action { implicit request =>
    orderStatusRepository.delete(id)
    Redirect(routes.OrderStatusController.getStatuses())
  }

  def updateStatus(id: Long): Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val stat = json.as[OrderStatus]
    val updated = orderStatusRepository.update(stat.id, stat)
    updated map {
      r => Ok(Json.toJson(stat))
    }
  }

  // -> FORM
  def createStatusForm(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.createStatus(statusForm))
  }

  def createStatusHandler() = Action.async { implicit request =>
  statusForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.createStatus(errorForm))
        )
      },
    status => {
      orderStatusRepository.create(status. orderStatusCode, status.orderStatusDescription).map { _ =>
          Ok("Success")
        }
      }
    )
  }

  def getFormStatuses(): Action[AnyContent] = Action.async { implicit request =>
    val stat = orderStatusRepository.list()
    stat.map( status => Ok(views.html.statuses(status)))
  }

  def updateStatusFrom(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val status = orderStatusRepository.getByID(id)
    status.map(o => {
      val form = updatedStatusForm.fill(UpdateStatusForm(o.id, o.orderStatusCode, o.orderStatusDescription))
      Ok(views.html.orderStatusUpdate(form))
    })
  }
  def updateStatusHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updatedStatusForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.orderStatusUpdate(errorForm))
        )
      },
      order => {
        orderStatusRepository.update(order.id, OrderStatus(order.id, order.orderStatusCode, order.orderStatusDescription)).map { _ =>
          Redirect(routes.OrderStatusController.updateStatus(order.id)).flashing("success" -> "Order status updated")
        }
      }
    )
  }
}
