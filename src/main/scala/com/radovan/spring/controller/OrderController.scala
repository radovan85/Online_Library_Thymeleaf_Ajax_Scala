package com.radovan.spring.controller

import java.util
import java.util.Optional
import java.lang.Double

import com.radovan.spring.dto.{BookDto, CartDto, CartItemDto, CustomerDto, DeliveryAddressDto, LoyaltyCardDto, OrderDto}
import com.radovan.spring.service.{BookService, CartItemService, CartService, CustomerService, DeliveryAddressService, LoyaltyCardService, OrderService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping}

@Controller
@RequestMapping(value = Array("/orders"))
class OrderController {

  @Autowired
  private var customerService: CustomerService = _

  @Autowired
  private var cartService: CartService = _

  @Autowired
  private var cartItemService: CartItemService = _

  @Autowired
  private var addressService: DeliveryAddressService = _

  @Autowired
  private var bookService: BookService = _

  @Autowired
  private var loyaltyCardService: LoyaltyCardService = _

  @Autowired
  private var orderService: OrderService = _

  @RequestMapping(value = Array("/confirmOrder/{cartId}"))
  def confirmOrder(@PathVariable("cartId") cartId: Integer, map: ModelMap): String = {
    val order: OrderDto = new OrderDto
    cartService.validateCart(cartId)
    val customer: CustomerDto = customerService.getCustomerByCartId(cartId)
    val allCartItems: util.List[CartItemDto] = cartItemService.listAllByCartId(cartId)
    val address: DeliveryAddressDto = addressService.getAddressById(customer.getDeliveryAddressId)
    val cart: CartDto = cartService.getCartByCartId(customer.getCartId)
    val allBooks: util.List[BookDto] = bookService.listAll
    val bookQuantity: Integer = cartItemService.getBookQuantity(cartId)
    val loyaltyCardId: Optional[Integer] = Optional.ofNullable(customer.getLoyaltyCardId)
    if (loyaltyCardId.isPresent) {
      val loyaltyCard: LoyaltyCardDto = loyaltyCardService.getCardByCardId(loyaltyCardId.get)
      map.put("discount", loyaltyCard.getDiscount)
    }
    else map.put("discount", 0.asInstanceOf[Integer])
    map.put("order", order)
    map.put("customer", customer)
    map.put("allCartItems", allCartItems)
    map.put("address", address)
    map.put("cart", cart)
    map.put("allBooks", allBooks)
    map.put("bookQuantity", bookQuantity)
    "fragments/orderConfirmation :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/processOrder"))
  def processOrder(map: ModelMap): String = {
    var pointsCollected: Integer = null
    var pointsSpent: Integer = 0
    var cardPoints: Integer = null
    val order: OrderDto = orderService.addOrder()
    val orderPrice: Double = order.getOrderPrice
    val customer: CustomerDto = customerService.getCustomer(order.getCustomerId)
    val loyaltyCardId: Optional[Integer] = Optional.ofNullable(customer.getLoyaltyCardId)
    if (loyaltyCardId.isPresent) {
      val loyaltyCard: LoyaltyCardDto = loyaltyCardService.getCardByCardId(loyaltyCardId.get)
      cardPoints = loyaltyCard.getPoints
      order.setDiscount(loyaltyCard.getDiscount)
      orderService.refreshOrder(order.getOrderId, order)
      pointsCollected = (orderPrice / 10).toInt
      if (pointsCollected >= 100) pointsCollected = 100
      if (cardPoints >= 100) {
        pointsSpent = 100
        cardPoints = cardPoints - pointsSpent
      }
      cardPoints = cardPoints + pointsCollected
      loyaltyCard.setPoints(cardPoints)
      if (loyaltyCard.getPoints >= 100) loyaltyCard.setDiscount(35)
      else loyaltyCard.setDiscount(0)
      loyaltyCardService.updateLoyaltyCard(loyaltyCardId.get, loyaltyCard)
    }
    map.put("discount", order.getDiscount)
    map.put("pointsCollected", pointsCollected)
    map.put("pointsSpent", pointsSpent)
    map.put("cardPoints", cardPoints)
    "fragments/orderCompleted :: ajaxLoadedContent"
  }
}

