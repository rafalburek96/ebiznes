package controllers
 
import javax.inject._
import models.{ OrderRepository, OrderedProducts, OrderedProductsRepository, ProductRepository }
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

case class CreateOrderedProductForm(var productID: Long, var productName: String, var quantity: Int, var orderID: Long)
case class UpdateOrderedProductForm(var id: Long, var productID: Long, var productName: String, var quantity: Int, var orderID: Long)

@Singleton
class OrderedProductsController @Inject() (orderedProductsRepo: OrderedProductsRepository, productRepository: ProductRepository, orderRepository: OrderRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val orderedProductsForm: Form[CreateOrderedProductForm] = Form {
    mapping(
      "productID" -> longNumber,
      "productName" -> nonEmptyText,
      "quantity" -> number,
      "orderID" -> longNumber)(CreateOrderedProductForm.apply)(CreateOrderedProductForm.unapply)
  }

  val updatedOrderedProductsForm: Form[UpdateOrderedProductForm] = Form {
    mapping(
      "id" -> longNumber,
      "productID" -> longNumber,
      "productName" -> nonEmptyText,
      "quantity" -> number,
      "orderID" -> longNumber)(UpdateOrderedProductForm.apply)(UpdateOrderedProductForm.unapply)
  }

  // -> JSON
  def createOrderedProduct: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val orderedProd = json.as[OrderedProducts]
    val result = orderedProductsRepo.create(orderedProd.productID, orderedProd.productName, orderedProd.quantity, orderedProd.orderID)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getOrderedProducts: Action[AnyContent] = Action.async {
    val list = orderedProductsRepo.list()
    list map { r =>
      Ok(Json.toJson(r))
    }
  }

  def removeOrderedProduct(id: Long): Action[AnyContent] = Action { implicit request =>
    orderedProductsRepo.delete(id)
    Redirect(routes.BasketController.getBaskets())
  }
  def updateOrderedProduct(id: Long) = Action.async { request =>
    val json = request.body.asJson.get
    val ordered = json.as[OrderedProducts]
    val res = orderedProductsRepo.update(ordered.id, ordered)
    res map { r => Ok(Json.toJson(ordered)) }
  }

  def getOrderedProdById(id: Long): Action[AnyContent] = Action.async {
    val orderedByID = orderedProductsRepo.getByID(id)
    orderedByID map { a =>
      Ok(Json.toJson(a))
    }
  }

  // -> FORM
  def createOrderedProductsForm(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.createOrderedProducts(orderedProductsForm))
  }

  def createOrderedProductsHandler() = Action.async { implicit request =>
    orderedProductsForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.createOrderedProducts(errorForm)))
      },
      op => {
        orderedProductsRepo.create(op.productID, op.productName, op.quantity, op.orderID).map { _ =>
          Ok("Success")
        }
      })
  }

  def getFormOrderedProducts(): Action[AnyContent] = Action.async { implicit request =>
    val o = orderedProductsRepo.list()
    o.map(op => Ok(views.html.orderedProducts(op)))
  }

  def updateOrderedProductFrom(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val order = orderedProductsRepo.getByID(id)
    order.map(o => {
      val form = updatedOrderedProductsForm.fill(UpdateOrderedProductForm(o.id, o.productID, o.productName, o.quantity, o.orderID))
      Ok(views.html.orderedProductsUpdate(form))
    })
  }
  def updateOrderedProductHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updatedOrderedProductsForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.orderedProductsUpdate(errorForm)))
      },
      order => {
        orderedProductsRepo.update(order.id, OrderedProducts(order.id, order.productID, order.productName, order.quantity, order.orderID)).map { _ =>
          Redirect(routes.OrderedProductsController.updateOrderedProduct(order.id)).flashing("success" -> "Ordered Products updated")
        }
      })
  }

}
