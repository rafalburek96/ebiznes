package controllers
 
import javax.inject._
import models.{ Product, ProductCategoryRepository, ProductRepository }
import play.api.data.Form
import play.api.mvc._
import play.api.data.Forms._
import play.api.libs.json._

import scala.concurrent.{ ExecutionContext, Future }

case class CreateProductForm(productName: String, productDescription: String, productPrice: Int, category: Long)
case class UpdateProductForm(id: Long, productName: String, productDescription: String, productPrice: Int, category: Long)

@Singleton
class ProductController @Inject() (categoryRepository: ProductCategoryRepository, productRepository: ProductRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "productName" -> nonEmptyText,
      "productDescription" -> nonEmptyText,
      "productPrice" -> number,
      "category" -> longNumber)(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  val updateProductForm: Form[UpdateProductForm] = Form {
    mapping(
      "id" -> longNumber,
      "productName" -> nonEmptyText,
      "productDescription" -> nonEmptyText,
      "productPrice" -> number,
      "category" -> longNumber)(UpdateProductForm.apply)(UpdateProductForm.unapply)
  }

  //  def index = Action {
  //    Ok(views.html.index("Your new application is ready."))
  //  }

  // -> JSON
  def createProduct: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val product = json.as[Product]
    val result = productRepository.create(product.productName, product.productDescription, product.productPrice, product.category)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getProducts: Action[AnyContent] = Action.async {
    val list = productRepository.list()
    list map { r =>
      Ok(Json.toJson(r))
    }
  }

  def removeProduct(id: Long): Action[AnyContent] = Action { implicit request =>
    productRepository.delete(id)
    Redirect(routes.ProductController.getProducts())
  }

  def updateProduct(id: Long): Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val prod = json.as[Product]
    val updated = productRepository.update(prod.id, prod)
    updated map {
      r => Ok(Json.toJson(prod))
    }
  }

  // -> FORM
  def createProductForm(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.createProducts(productForm))
  }

  def createProductHandler() = Action.async { implicit request =>
    productForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.createProducts(errorForm)))
      },
      product => {
        productRepository.create(product.productName, product.productDescription, product.productPrice, product.category).map { _ =>
          Ok("Success")
        }
      })
  }

  def getFormProducts(): Action[AnyContent] = Action.async { implicit request =>
    val prod = productRepository.list()
    prod.map(product => Ok(views.html.products(product)))
  }

  def updateProductFrom(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val product = productRepository.getByID(id)
    product.map(o => {
      val form = updateProductForm.fill(UpdateProductForm(o.id, o.productName, o.productDescription, o.productPrice, o.category))
      Ok(views.html.productUpdate(form))
    })
  }

  def updateProductHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updateProductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.productUpdate(errorForm)))
      },
      product => {
        productRepository.update(product.id, Product(product.id, product.productName, product.productDescription, product.productPrice, product.category)).map { _ =>
          Redirect(routes.PaymentController.updatePayment(product.id)).flashing("success" -> "Payment updated")
        }
      })
  }
}
