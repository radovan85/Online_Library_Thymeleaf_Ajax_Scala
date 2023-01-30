package com.radovan.spring.controller

import java.util

import com.radovan.spring.dto.{CustomerDto, LoyaltyCardDto, LoyaltyCardRequestDto, UserDto}
import com.radovan.spring.service.{CustomerService, LoyaltyCardService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod}

@Controller
@RequestMapping(Array("/loyaltyCards"))
class LoyaltyCardController {

  @Autowired
  private var loyaltyCardService: LoyaltyCardService = _

  @Autowired
  private var userService: UserService = _

  @Autowired
  private var customerService: CustomerService = _

  @RequestMapping(value = Array("/createCardRequest"), method = Array(RequestMethod.POST))
  def createCardRequest: String = {
    loyaltyCardService.addCardRequest()
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/cardRequestSent"))
  def cardRequestSent: String = "fragments/loyaltyCardRequestSent :: ajaxLoadedContent"

  @RequestMapping(value = Array("/cardInfo"))
  def loyaltyCardInfo(map: ModelMap): String = {
    val authUser: UserDto = userService.getCurrentUser
    val customer: CustomerDto = customerService.getCustomerByUserId(authUser.getId)
    val cardRequest: LoyaltyCardRequestDto = loyaltyCardService.getRequestByCustomerId(customer.getCustomerId)
    val allCards: util.List[LoyaltyCardDto] = loyaltyCardService.listAllLoyaltyCards
    map.put("customer", customer)
    map.put("cardRequest", cardRequest)
    map.put("allCards", allCards)
    "fragments/loyaltyCardDetails :: ajaxLoadedContent"
  }
}
