package controllers
 
import javax.inject._
import models.{ ProductCategory, ProductCategoryRepository }
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.Forms.mapping
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

case class CreateProductCategoryForm(categoryName: String, categoryDescription: String)
case class UpdateProductCategoryForm(id: Long, categoryName: String, categoryDescription: String)

@Singleton
class ProductCategoryController @Inject() (productCategoryRepository: ProductCategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val productCategoryForm: Form[CreateProductCategoryForm] = Form {
    mapping(
      "categoryName" -> nonEmptyText,
      "categoryDescription" -> nonEmptyText)(CreateProductCategoryForm.apply)(CreateProductCategoryForm.unapply)
  }

  val updatedproductCategoryForm: Form[UpdateProductCategoryForm] = Form {
    mapping(
      "id" -> longNumber,
      "categoryName" -> nonEmptyText,
      "categoryDescription" -> nonEmptyText)(UpdateProductCategoryForm.apply)(UpdateProductCategoryForm.unapply)
  }

  // -> JSON
  def createCategory: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val category = json.as[ProductCategory]
    val result = productCategoryRepository.create(category.categoryName, category.categoryDescription)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getCategories: Action[AnyContent] = Action.async {
    val list = productCategoryRepository.list()
    list map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getCategoryById(id: Long): Action[AnyContent] = Action.async {
    val cat = productCategoryRepository.getByID(id)
    cat map { a =>
      Ok(Json.toJson(a))
    }
  }

  def removeCategory(id: Long): Action[AnyContent] = Action { implicit request =>
    productCategoryRepository.delete(id)
    Redirect(routes.ProductCategoryController.getCategories())
  }

  def updateCategory(id: Long): Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val cat = json.as[ProductCategory]
    val updated = productCategoryRepository.update(cat.id, cat)
    updated map {
      r => Ok(Json.toJson(cat))
    }
  }

  // -> FORM
  def createCategoryForm(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.createCategory(productCategoryForm))
  }

  def createCategoryHandler() = Action.async { implicit request =>
    productCategoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.createCategory(errorForm)))
      },
      category => {
        productCategoryRepository.create(category.categoryName, category.categoryDescription).map { _ =>
          Ok("Success")
        }
      })
  }

  def getFormCategories(): Action[AnyContent] = Action.async { implicit request =>
    val cat = productCategoryRepository.list()
    cat.map(category => Ok(views.html.categories(category)))
  }

  def updateCategoryFrom(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val category = productCategoryRepository.getByID(id)
    category.map(o => {
      val form = updatedproductCategoryForm.fill(UpdateProductCategoryForm(o.id, o.categoryName, o.categoryDescription))
      Ok(views.html.categoryUpdate(form))
    })
  }

  def updateCategoryHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updatedproductCategoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.categoryUpdate(errorForm)))
      },
      category => {
        productCategoryRepository.update(category.id, ProductCategory(category.id, category.categoryName, category.categoryDescription)).map { _ =>
          Redirect(routes.ProductCategoryController.updateCategory(category.id)).flashing("success" -> "Category updated")
        }
      })
  }
}
