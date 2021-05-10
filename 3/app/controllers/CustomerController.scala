package controllers
 
import javax.inject._
import models.CustomerRepository
import play.api.mvc._

@Singleton
class CustomerController @Inject() (cc: ControllerComponents, customerRepository: CustomerRepository) extends AbstractController(cc) {

  def getCustomers = Action { request =>
    Ok("Got GET [" + request + "]")
  }

  def removeCustomer: Action[AnyContent] = Action { request =>
    Ok("Got DELETE [" + request + "]")
  }

  def updateCustomer: Action[AnyContent] = Action { request =>
    Ok("Got PUT [" + request + "]")
  }

  def createCustomer: Action[AnyContent] = Action { request =>
    Ok("Got POST [" + request + "]")
  }
}

