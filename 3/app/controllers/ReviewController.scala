package controllers
 
import javax.inject._
import models.{ CustomerRepository, ProductRepository, Review, ReviewRepository }
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.Forms.mapping
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ ExecutionContext, Future }

case class CreateReviewForm(reviewRating: Int, productID: Long, customerID: Long)
case class UpdateReviewForm(id: Long, reviewRating: Int, productID: Long, customerID: Long)

@Singleton
class ReviewController @Inject() (reviewRepository: ReviewRepository, productRepository: ProductRepository, customerRepository: CustomerRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val reviewForm: Form[CreateReviewForm] = Form {
    mapping(
      "reviewRating" -> number,
      "productID" -> longNumber,
      "customerID" -> longNumber)(CreateReviewForm.apply)(CreateReviewForm.unapply)
  }

  val updatedReviewForm: Form[UpdateReviewForm] = Form {
    mapping(
      "id" -> longNumber,
      "reviewRating" -> number,
      "productID" -> longNumber,
      "customerID" -> longNumber)(UpdateReviewForm.apply)(UpdateReviewForm.unapply)
  }

  // -> JSON
  def createReview: Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val review = json.as[Review]
    val result = reviewRepository.create(review.reviewRating, review.productID, review.customerID)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getReviews(): Action[AnyContent] = Action.async {
    val list = reviewRepository.list()
    list map { r =>
      Ok(Json.toJson(r))
    }
  }

  def removeReview(id: Long): Action[AnyContent] = Action { implicit request =>
    reviewRepository.delete(id)
    Redirect(routes.ReviewController.getReviews())
  }

  def updateReview(id: Long): Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val rev = json.as[Review]
    val updated = reviewRepository.update(rev.id, rev)
    updated map {
      r => Ok(Json.toJson(rev))
    }
  }

  // -> FORM
  def createReviewForm(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.createReview(reviewForm))
  }

  def createReviewHandler() = Action.async { implicit request =>
    reviewForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.createReview(errorForm)))
      },
      rev => {
        reviewRepository.create(rev.reviewRating, rev.productID, rev.customerID).map { _ =>
          Ok("Success")
        }
      })
  }

  def getFormReviews(): Action[AnyContent] = Action.async { implicit request =>
    val rev = reviewRepository.list()
    rev.map(review => Ok(views.html.reviews(review)))
  }

  def updateReviewFrom(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val review = reviewRepository.getByID(id)
    review.map(o => {
      val form = updatedReviewForm.fill(UpdateReviewForm(o.id, o.reviewRating, o.productID, o.customerID))
      Ok(views.html.reviewUpdate(form))
    })
  }

  def updateReviewHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updatedReviewForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.reviewUpdate(errorForm)))
      },
      review => {
        reviewRepository.update(review.id, Review(review.id, review.reviewRating, review.productID, review.customerID)).map { _ =>
          Redirect(routes.ReviewController.updateReview(review.id)).flashing("success" -> "Review updated")
        }
      })
  }
}
