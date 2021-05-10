package controllers
 
import javax.inject._
import play.api.data.Form
import models.{Address, AddressRepository, CustomerRepository}
import play.api.data.Forms._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class CreateAddressForm(var street: String, var flatNumber: Int, var zipCode: String)
case class UpdateAddressFrom(var id: Long, var street: String, var flatNumber: Int, var zipCode: String)

@Singleton
class AddressController @Inject()(addressRepo: AddressRepository, customerRepo: CustomerRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val addressForm: Form[CreateAddressForm] = Form {
    mapping(
      "street" -> nonEmptyText,
      "flatNumber" -> number,
      "zipCode" -> nonEmptyText,
    )(CreateAddressForm.apply)(CreateAddressForm.unapply)
  }

  val updateAddressForm: Form[UpdateAddressFrom] = Form {
    mapping(
      "id" -> longNumber,
      "street" -> nonEmptyText,
      "flatNumber" -> number,
      "zipCode" -> nonEmptyText,
    )(UpdateAddressFrom.apply)(UpdateAddressFrom.unapply)
  }

  // -> JSON
  def createAddress(): Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val address = json.as[Address]
    val result = addressRepo.create(address.street, address.flatNumber, address.zipCode)
    result map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getAllAddresses: Action[AnyContent] = Action.async {
    val list = addressRepo.list()
    list map { r =>
      Ok(Json.toJson(r))
    }
  }

  def getAddress(id: Long): Action[AnyContent] = Action.async {
    val addressByID = addressRepo.getByID(id)
    addressByID map { a =>
      Ok(Json.toJson(a))
    }
  }

  def updateAddress(id: Long): Action[AnyContent] = Action.async { request =>
    val json = request.body.asJson.get
    val addr = json.as[Address]
    val updated = addressRepo.update(addr.id, addr)
    updated map {
      r => Ok(Json.toJson(addr))
    }
  }

  def removeAddress(id: Long): Action[AnyContent] = Action { implicit request =>
    addressRepo.delete(id)
    Redirect(routes.AddressController.getAllAddresses())
  }

  // -> FORM
  def createAddresForm(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.createAddress(addressForm))
  }

  def createAddressHandler() = Action.async { implicit request =>
    addressForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.createAddress(errorForm))
        )
      },
      address => {
        addressRepo.create(address.street, address.flatNumber, address.zipCode).map { _ =>
          Ok("Success")
        }
      }
    )
  }

  def getFormAddresses(): Action[AnyContent] = Action.async { implicit request =>
    val addr = addressRepo.list()
    addr.map( address => Ok(views.html.addresses(address)))
  }

  def updateAddressFrom(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
      val address = addressRepo.getByID(id)
      address.map(addr => {
        val form = updateAddressForm.fill(UpdateAddressFrom(addr.id, addr.street, addr.flatNumber, addr.zipCode))
        Ok(views.html.addressUpdate(form))
      })
    }
  def updateAddressHandler(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    updateAddressForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.addressUpdate(errorForm))
        )
      },
    address => {
      addressRepo.update(address.id, Address(address.id, address.street, address.flatNumber, address.zipCode)).map { _ =>
        Redirect(routes.AddressController.updateAddress(address.id)).flashing("success" -> "Address updated")
      }
    }
    )
  }
}


