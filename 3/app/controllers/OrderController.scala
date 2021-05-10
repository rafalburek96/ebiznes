package controllers
 
import javax.inject._
import play.api.data.Form
import models.{ BasketRepository, CustomerRepository, Order, OrderRepository, OrderedProductsRepository}
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class CreateOrderForm(customerID: Long, basketID: Long, deliveryDate: String, purchaseDate: String, orderStatusCode: Int)
case class UpdateOrderForm(id:Long, customerID: Long, basketID: Long, deliveryDate: String, purchaseDate: String, orderStatusCode: Int)


@Singleton
class OrderController @Inject()(orderRepo: OrderRepository, basketRepo: BasketRepository, customerRepo: CustomerRepository, orderedProductsRepository: OrderedProductsRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "customerID" -> longNumber,
      "basketID" -> longNumber,
      "deliveryDate" -> nonEmptyText,
      "purchaseDate" -> nonEmptyText,
      "orderStatusCode" -> number,
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  val updateOrderForm: Form[UpdateOrderForm] = Form {
    mapping(
      "id" -> longNumber,
      "customerID" -> longNumber,
      "basketID" -> longNumber,
      "deliveryDate" -> nonEmptyText,
      "purchaseDate" -> nonEmptyText,
      "orderStatusCode" -> number,
    )(UpdateOrderForm.apply)(UpdateOrderForm.unapply)
  }

  // -> JSON
  def createOrder: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val order = json.as[Order]
    val result = orderRepo.create(order.customerID, order.basketID, order.deliveryDate, order.purchaseDate, order.orderStatusCode)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getOrders: Action[AnyContent] = Action.async {
    val list = orderRepo.list()
    list map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getOrderById(id: Long): Action[AnyContent] = Action.async {
    val orderByID = orderRepo.getByID(id)
    orderByID map { a =>
      Ok(Json.toJson(a))
    }
  }

  def removeOrder(id: Long): Action[AnyContent] = Action { implicit request =>
    orderRepo.delete(id)
    Redirect(routes.OrderController.getOrders())
  }

  def updateOrder(id: Long): Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val ord = json.as[Order]
    val updated = orderRepo.update(ord.id, ord)
    updated map {
      r => Ok(Json.toJson(ord))
    }
  }



  // -> FORM
  def createOrderForm(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.createOrder(orderForm))
  }

  def createOrderHandler() = Action.async { implicit request =>
    orderForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.createOrder(errorForm))
        )
      },
      order => {
        orderRepo.create(order.customerID, order.basketID, order.deliveryDate, order.purchaseDate, order.orderStatusCode).map { _ =>
          Ok("Success")
        }
      }
    )
  }

  def getFormOrders(): Action[AnyContent] = Action.async { implicit request =>
    val ord = orderRepo.list()
    ord.map( order => Ok(views.html.orders(order)))
  }

  def updateOrderFrom(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val order = orderRepo.getByID(id)
    order.map(o => {
      val form = updateOrderForm.fill(UpdateOrderForm(o.id, o.customerID, o.basketID, o.deliveryDate, o.purchaseDate, o.orderStatusCode))
      Ok(views.html.orderUpdate(form))
    })
  }

  def updateOrderHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updateOrderForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.orderUpdate(errorForm))
        )
      },
      order => {
        orderRepo.update(order.id, Order(order.id, order.customerID, order.basketID, order.deliveryDate, order.purchaseDate, order.orderStatusCode)).map { _ =>
          Redirect(routes.OrderController.updateOrder(order.id)).flashing("success" -> "Order updated")
        }
      }
    )
  }

}
