package com.radovan.spring.controller

import java.util
import java.util.Optional
import java.lang.Double

import com.radovan.spring.dto.{BookDto, CartDto, CartItemDto, CustomerDto, LoyaltyCardDto, UserDto}
import com.radovan.spring.service.{BookService, CartItemService, CartService, CustomerService, LoyaltyCardService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMapping, RequestMethod}

@Controller
@RequestMapping(value = Array("/cart"))
class CartController {

  @Autowired
  private var cartService: CartService = _

  @Autowired
  private var customerService: CustomerService = _

  @Autowired
  private var userService: UserService = _

  @Autowired
  private var cartItemService: CartItemService = _

  @Autowired
  private var bookService: BookService = _

  @Autowired
  private var loyaltyCardService: LoyaltyCardService = _

  @RequestMapping(value = Array("/addToCart"), method = Array(RequestMethod.POST))
  def addCartItem(@ModelAttribute("cartItem") cartItem: CartItemDto): String = {
    val bookId: Integer = cartItem.getBookId
    val authUser: UserDto = userService.getCurrentUser
    val customer: CustomerDto = customerService.getCustomerByUserId(authUser.getId)
    val cart: CartDto = cartService.getCartByCartId(customer.getCartId)
    val loyaltyCardId: Optional[Integer] = Optional.ofNullable(customer.getLoyaltyCardId)
    val book: BookDto = bookService.getBookById(bookId)
    if (loyaltyCardId.isPresent) {
      val loyaltyCard: LoyaltyCardDto = loyaltyCardService.getCardByCardId(loyaltyCardId.get)
      val discount: Integer = loyaltyCard.getDiscount
      val existingCartItem: Optional[CartItemDto] = Optional.ofNullable(cartItemService.getCartItemByCartIdAndBookId(cart.getCartId, bookId))
      if (existingCartItem.isPresent) {
        cartItem.setCartItemId(existingCartItem.get.getCartItemId)
        cartItem.setCartId(cart.getCartId)
        cartItem.setQuantity(existingCartItem.get.getQuantity + cartItem.getQuantity)
        if (cartItem.getQuantity > 50) cartItem.setQuantity(50)
        if (discount == 0) cartItem.setPrice(book.getPrice * cartItem.getQuantity)
        else {
          var cartPrice: Double = book.getPrice * cartItem.getQuantity
          cartPrice = cartPrice - ((cartPrice * discount) / 100)
          cartItem.setPrice(cartPrice)
        }
        cartItemService.addCartItem(cartItem)
        cartService.refreshCartState(cart.getCartId)
      }
      else {
        cartItem.setQuantity(cartItem.getQuantity)
        if (cartItem.getQuantity > 50) cartItem.setQuantity(50)
        if (discount == 0) cartItem.setPrice(book.getPrice * cartItem.getQuantity)
        else {
          var cartPrice: Double = book.getPrice * cartItem.getQuantity
          cartPrice = cartPrice - ((cartPrice * discount) / 100)
          cartItem.setPrice(cartPrice)
        }
        cartItem.setCartId(cart.getCartId)
        cartItemService.addCartItem(cartItem)
        cartService.refreshCartState(cart.getCartId)
      }
    }
    else {
      val existingCartItem: Optional[CartItemDto] = Optional.ofNullable(cartItemService.getCartItemByCartIdAndBookId(cart.getCartId, bookId))
      if (existingCartItem.isPresent) {
        cartItem.setCartItemId(existingCartItem.get.getCartItemId)
        cartItem.setCartId(cart.getCartId)
        cartItem.setQuantity(existingCartItem.get.getQuantity + cartItem.getQuantity)
        if (cartItem.getQuantity > 50) cartItem.setQuantity(50)
        cartItem.setPrice(book.getPrice * cartItem.getQuantity)
        cartItemService.addCartItem(cartItem)
        cartService.refreshCartState(cart.getCartId)
      }
      else {
        cartItem.setQuantity(cartItem.getQuantity)
        if (cartItem.getQuantity > 50) cartItem.setQuantity(50)
        cartItem.setCartId(cart.getCartId)
        cartItem.setPrice(book.getPrice * cartItem.getQuantity)
        cartItemService.addCartItem(cartItem)
        cartService.refreshCartState(cart.getCartId)
      }
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/addItemCompleted"))
  def addItemCompleted(): String = "fragments/itemAdded :: ajaxLoadedContent"

  @RequestMapping(value = Array("/getCart"))
  def cartDetails(map: ModelMap): String = {
    val authUser: UserDto = userService.getCurrentUser
    val customer: CustomerDto = customerService.getCustomerByUserId(authUser.getId)
    val cart: CartDto = cartService.getCartByCartId(customer.getCartId)
    val allCartItems: util.List[CartItemDto] = cartItemService.listAllByCartId(customer.getCartId)
    val allBooks: util.List[BookDto] = bookService.listAll
    val loyaltyCardId: Optional[Integer] = Optional.ofNullable(customer.getLoyaltyCardId)
    if (loyaltyCardId.isPresent) {
      val loyaltyCard: LoyaltyCardDto = loyaltyCardService.getCardByCardId(loyaltyCardId.get)
      map.put("discount", loyaltyCard.getDiscount)
    }
    else map.put("discount", 0.asInstanceOf[Integer])
    map.put("allCartItems", allCartItems)
    map.put("allBooks", allBooks)
    map.put("cart", cart)
    "fragments/cart :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteItem/{cartId}/{itemId}"))
  def deleteCartItem(@PathVariable("cartId") cartId: Integer, @PathVariable("itemId") itemId: Integer): String = {
    cartItemService.removeCartItem(cartId, itemId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteAllItems/{cartId}"))
  def deleteAllCartItems(@PathVariable("cartId") cartId: Integer): String = {
    cartItemService.eraseAllCartItems(cartId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/invalidCart"))
  def invalidCart: String = "fragments/invalidCart :: ajaxLoadedContent"
}

