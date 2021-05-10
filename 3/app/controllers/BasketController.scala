package controllers
 
import javax.inject._
import play.api.data.Form
import models.{ Basket, BasketRepository, CustomerRepository }
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

case class CreateBasketForm(var customerID: Long, var basketDateAdded: String, var isBasketCompleted: Boolean)
case class UpdateBasketForm(var id: Long, var customerID: Long, var basketDateAdded: String, var isBasketCompleted: Boolean)

@Singleton
class BasketController @Inject() (basketRepository: BasketRepository, customerRepository: CustomerRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val basketForm: Form[CreateBasketForm] = Form {
    mapping(
      "customerID" -> longNumber,
      "basketDateAdded" -> nonEmptyText,
      "isBasketCompleted" -> boolean)(CreateBasketForm.apply)(CreateBasketForm.unapply)
  }

  val updatedBasketForm: Form[UpdateBasketForm] = Form {
    mapping(
      "id" -> longNumber,
      "customerID" -> longNumber,
      "basketDateAdded" -> nonEmptyText,
      "isBasketCompleted" -> boolean)(UpdateBasketForm.apply)(UpdateBasketForm.unapply)
  }

  // -> JSON
  def createBasket: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val basket = json.as[Basket]
    val result = basketRepository.create(basket.customerID, basket.basketDateAdded, basket.isBasketCompleted)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getBaskets: Action[AnyContent] = Action.async {
    val list = basketRepository.list()
    list map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getBasketById(id: Long): Action[AnyContent] = Action.async {
    val basketByID = basketRepository.getByID(id)
    basketByID map { a =>
      Ok(Json.toJson(a))
    }
  }

  def updateBasket(id: Long): Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val bask = json.as[Basket]
    val updated = basketRepository.update(bask.id, bask)
    updated map {
      r => Ok(Json.toJson(bask))
    }
  }

  // -> FORM
  def createBasketForm(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.createBasket(basketForm))
  }

  def createBasketHandler() = Action.async { implicit request =>
    basketForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.createBasket(errorForm)))
      },
      basket => {
        basketRepository.create(basket.customerID, basket.basketDateAdded, basket.isBasketCompleted).map { _ =>
          Ok("Success")
        }
      })
  }

  def getFormBaskets(): Action[AnyContent] = Action.async { implicit request =>
    val bask = basketRepository.list()
    bask.map(basket => Ok(views.html.baskets(basket)))
  }

  def removeBasket(id: Long): Action[AnyContent] = Action { implicit request =>
    basketRepository.delete(id)
    Redirect(routes.BasketController.getBaskets())
  }

  def updateBasketFrom(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val basket = basketRepository.getByID(id)
    basket.map(bask => {
      val form = updatedBasketForm.fill(UpdateBasketForm(bask.id, bask.customerID, bask.basketDateAdded, bask.isBasketCompleted))
      Ok(views.html.basketUpdate(form))
    })
  }
  def updateBasketHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updatedBasketForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.basketUpdate(errorForm)))
      },
      basket => {
        basketRepository.update(basket.id, Basket(basket.id, basket.customerID, basket.basketDateAdded, basket.isBasketCompleted)).map { _ =>
          Redirect(routes.BasketController.updateBasket(basket.id)).flashing("success" -> "Basket updated")
        }
      })
  }
}
