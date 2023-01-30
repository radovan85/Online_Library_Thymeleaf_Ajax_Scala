package com.radovan.spring.controller

import java.security.Principal
import java.util.Optional

import com.radovan.spring.entity.UserEntity
import com.radovan.spring.exceptions.{InvalidUserException, SuspendedUserException}
import com.radovan.spring.model.RegistrationForm
import com.radovan.spring.service.{CustomerService, DeliveryAddressService, PersistenceLoginService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, RequestMapping, RequestMethod, RequestParam}
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class MainController {

  @Autowired
  private var customerService:CustomerService = _

  @Autowired
  private var persistenceService:PersistenceLoginService = _

  @Autowired
  private var userService:UserService = _

  @Autowired
  private var addressService:DeliveryAddressService = _

  @RequestMapping(value = Array("/"), method = Array(RequestMethod.GET))
  def sayIndex = "index"

  @RequestMapping(value = Array("/home"), method = Array(RequestMethod.GET))
  def home = "fragments/homePage :: ajaxLoadedContent"

  @RequestMapping(value = Array("/login"), method = Array(RequestMethod.GET))
  def login(@RequestParam(value = "error", required = false) error: String, @RequestParam(value = "logout", required = false) logout: String, map: ModelMap): String = {
    if (error != null) map.put("error", "Invalid username and Password")
    if (logout != null) map.put("logout", "You have logged out successfully")
    "fragments/login :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/userRegistration"), method = Array(RequestMethod.GET))
  def register(map: ModelMap): String = {
    val tempForm = new RegistrationForm
    map.put("tempForm", tempForm)
    "fragments/registration :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/userRegistration"), method = Array(RequestMethod.POST))
  def storeUser(@ModelAttribute("tempForm") form: RegistrationForm): String = {
    customerService.storeCustomer(form)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/registerComplete"), method = Array(RequestMethod.GET))
  def registrationCompl = "fragments/registration_completed :: ajaxLoadedContent"

  @RequestMapping(value = Array("/registerFail"), method = Array(RequestMethod.GET))
  def registrationFail = "fragments/registration_failed :: ajaxLoadedContent"

  @RequestMapping(value = Array("/loggedout"), method = Array(RequestMethod.POST))
  def logout(redirectAttributes: RedirectAttributes): String = {
    SecurityContextHolder.clearContext()
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/loginErrorPage"), method = Array(RequestMethod.GET))
  def logError(map: ModelMap): String = {
    map.put("alert", "Invalid username or password!")
    "fragments/loginError :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/loginPassConfirm"), method = Array(RequestMethod.POST))
  def confirmLoginPass(principal: Principal): String = {
    val authPrincipal = Optional.ofNullable(principal)
    if (!authPrincipal.isPresent) {
      val error = new Error("Invalid user")
      throw new InvalidUserException(error)
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/suspensionChecker"), method = Array(RequestMethod.POST))
  def checkForSuspension: String = {
    val authUser = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    if (authUser.getEnabled == 0.toByte) {
      val error = new Error("Account suspended!")
      throw new SuspendedUserException(error)
    }
    persistenceService.addPersistenceLogin()
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/suspensionPage"), method = Array(RequestMethod.GET))
  def suspensionAlert(map: ModelMap): String = {
    map.put("alert", "Account suspended!")
    "fragments/loginError :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/aboutUs"), method = Array(RequestMethod.GET))
  def aboutPage = "fragments/about :: ajaxLoadedContent"

  @Secured(value = Array("ROLE_USER"))
  @RequestMapping(value = Array("/accountInfo"))
  def userAccountInfo(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val address = addressService.getAddressById(customer.getDeliveryAddressId)
    map.put("authUser", authUser)
    map.put("address", address)
    "fragments/accountDetails :: ajaxLoadedContent"
  }
}

