package com.radovan.spring.controller

import java.nio.file.{Files, Paths}
import java.util.Optional

import com.radovan.spring.dto.{BookDto, BookGenreDto}
import com.radovan.spring.exceptions.ImagePathException
import com.radovan.spring.service.{BookGenreService, BookService, CartItemService, CartService, CustomerService, DeliveryAddressService, LoyaltyCardService, OrderAddressService, OrderItemService, OrderService, PersistenceLoginService, ReviewService, UserService, WishListService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMapping, RequestMethod, RequestParam}
import org.springframework.web.multipart.MultipartFile

import scala.collection.JavaConverters._

@Controller
@RequestMapping(value = Array("/admin"))
class AdminController {

  @Autowired
  private var bookService:BookService = _

  @Autowired
  private var genreService:BookGenreService = _

  @Autowired
  private var reviewService:ReviewService = _

  @Autowired
  private var customerService:CustomerService = _

  @Autowired
  private var userService:UserService = _

  @Autowired
  private var loyaltyCardService:LoyaltyCardService = _

  @Autowired
  private var orderService:OrderService = _

  @Autowired
  private var orderItemService:OrderItemService = _

  @Autowired
  private var addressService:DeliveryAddressService = _

  @Autowired
  private var persistenceService:PersistenceLoginService = _

  @Autowired
  private var cartItemService:CartItemService = _

  @Autowired
  private var orderAddressService:OrderAddressService = _

  @Autowired
  private var cartService:CartService = _

  @Autowired
  private var wishListService:WishListService = _

  @RequestMapping(value = Array("/createBook"))
  def renderBookForm(map: ModelMap): String = {
    val book = new BookDto
    val allGenres = genreService.listAll
    map.put("book", book)
    map.put("allGenres", allGenres)
    "fragments/bookForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/createBook"), method = Array(RequestMethod.POST))
  @throws[Throwable]
  def createBook(@ModelAttribute("book") book: BookDto, map: ModelMap, @RequestParam("bookImage") file: MultipartFile, @RequestParam("imgName") imgName: String): String = {
    val fileLocation = "C:\\Users\\Radovan\\IdeaProjects\\Online_Library_Project_Scala\\src\\main\\resources\\static\\images\\bookImages"
    var imageUUID:String = null
    val locationPath = Paths.get(fileLocation)
    if (!Files.exists(locationPath)) {
      val error = new Error("Invalid file path!")
      throw new ImagePathException(error)
    }
    imageUUID = file.getOriginalFilename
    val fileNameAndPath = Paths.get(fileLocation, imageUUID)
    if (file != null && !file.isEmpty) {
      Files.write(fileNameAndPath, file.getBytes)
      System.out.println("IMage Save at:" + fileNameAndPath.toString)
    }
    else imageUUID = imgName
    book.setImageName(imageUUID)
    bookService.addBook(book)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteBook/{bookId}"))
  @throws[Throwable]
  def deleteBook(@PathVariable("bookId") bookId: Integer): String = {
    val book = bookService.getBookById(bookId)
    val path = Paths.get("C:\\Users\\Radovan\\IdeaProjects\\Online_Library_Project_Scala\\src\\main\\resources\\static\\images\\bookImages\\" + book.getImageName)
    if (Files.exists(path)) Files.delete(path)
    else {
      val error = new Error("Invalid file path!")
      throw new ImagePathException(error)
    }
    cartItemService.eraseAllByBookId(bookId)
    bookService.removeBookFromAllWishlist(bookId)
    bookService.deleteBook(bookId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/updateBook/{bookId}"))
  def renderUpdateForm(@PathVariable("bookId") bookId: Integer, map: ModelMap): String = {
    val book = new BookDto
    val currentBook = bookService.getBookById(bookId)
    val allGenres = genreService.listAll
    map.put("book", book)
    map.put("currentBook", currentBook)
    map.put("allGenres", allGenres)
    "fragments/bookUpdateForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/createGenre"))
  def renderGenreForm(map: ModelMap): String = {
    val genre = new BookGenreDto
    map.put("genre", genre)
    "fragments/genreForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/createGenre"), method = Array(RequestMethod.POST))
  def createGenre(@ModelAttribute("genre") genre: BookGenreDto, map: ModelMap): String = {
    genreService.addGenre(genre)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allGenres"))
  def listAllGenres(map: ModelMap): String = {
    val allGenres = genreService.listAll
    map.put("allGenres", allGenres)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/genreList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteGenre/{genreId}"))
  def removeGenre(@PathVariable("genreId") genreId: Integer): String = {
    genreService.deleteGenre(genreId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/updateGenre/{genreId}"))
  def updateGenre(@PathVariable("genreId") genreId: Integer, map: ModelMap): String = {
    val genre = new BookGenreDto
    val currentGenre = genreService.getGenreById(genreId)
    map.put("genre", genre)
    map.put("currentGenre", currentGenre)
    "fragments/genreUpdateForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allReviews"))
  def reviewList(map: ModelMap): String = {
    val approvedReviews = reviewService.listAllAuthorized
    val allBooks = bookService.listAll
    val allCustomers = customerService.getAllCustomers
    val allUsers = userService.listAllUsers
    map.put("approvedReviews", approvedReviews)
    map.put("allBooks", allBooks)
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/reviewList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allRequestedReviews"))
  def requestedReviewsList(map: ModelMap): String = {
    val pendingReviews = reviewService.listAllOnHold
    val allBooks = bookService.listAll
    val allCustomers = customerService.getAllCustomers
    val allUsers = userService.listAllUsers
    map.put("pendingReviews", pendingReviews)
    map.put("allBooks", allBooks)
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/reviewRequestsList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/pendingReviewDetails/{reviewId}"))
  def getPendingReview(@PathVariable("reviewId") reviewId: Integer, map: ModelMap): String = {
    val currentReview = reviewService.getReviewById(reviewId)
    val tempCustomer = customerService.getCustomer(currentReview.getAuthorId)
    val tempUser = userService.getUserById(tempCustomer.getUserId)
    val currentBook = bookService.getBookById(currentReview.getBookId)
    map.put("currentReview", currentReview)
    map.put("tempUser", tempUser)
    map.put("currentBook", currentBook)
    "fragments/pendingReviewDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/reviewDetails/{reviewId}"))
  def getReviewDetails(@PathVariable("reviewId") reviewId: Integer, map: ModelMap): String = {
    val currentReview = reviewService.getReviewById(reviewId)
    val tempCustomer = customerService.getCustomer(currentReview.getAuthorId)
    val tempUser = userService.getUserById(tempCustomer.getUserId)
    val currentBook = bookService.getBookById(currentReview.getBookId)
    map.put("currentReview", currentReview)
    map.put("tempUser", tempUser)
    map.put("currentBook", currentBook)
    "fragments/reviewDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/authorizeReview/{reviewId}"), method = Array(RequestMethod.POST))
  def reviewAuthorization(@PathVariable("reviewId") reviewId: Integer): String = {
    reviewService.authorizeReview(reviewId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/rejectReview/{reviewId}"))
  def rejectReview(@PathVariable("reviewId") reviewId: Integer): String = {
    reviewService.deleteReview(reviewId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/checkCardRequests"))
  def allCardRequests(map: ModelMap): String = {
    val allRequests = loyaltyCardService.listAllCardRequests
    val allCustomers = customerService.getAllCustomers
    val allUsers = userService.listAllUsers
    map.put("allRequests", allRequests)
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/cardRequestList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/getAllCards"))
  def getAllCards(map: ModelMap): String = {
    val allCards = loyaltyCardService.listAllLoyaltyCards
    val allCustomers = customerService.getAllCustomers
    val allUsers = userService.listAllUsers
    map.put("allCards", allCards)
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/loyaltyCardList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/authorizeCard/{cardRequestId}"))
  def authorizeLoyaltyCard(@PathVariable("cardRequestId") cardRequestId: Integer, map: ModelMap): String = {
    loyaltyCardService.authorizeRequest(cardRequestId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/rejectCard/{cardRequestId}"))
  def rejectLoyaltyCard(@PathVariable("cardRequestId") cardRequestId: Integer, map: ModelMap): String = {
    loyaltyCardService.rejectRequest(cardRequestId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allOrders"), method = Array(RequestMethod.GET))
  def listAllOrders(map: ModelMap): String = {
    val allOrders = orderService.listAll
    val allCustomers = customerService.getAllCustomers
    val allUsers = userService.listAllUsers
    map.put("allOrders", allOrders)
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/orderList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allOrders/{customerId}"))
  def allOrdersByCustomerId(@PathVariable("customerId") customerId: Integer, map: ModelMap): String = {
    val allOrders = orderService.listAllByCustomerId(customerId)
    val customer = customerService.getCustomer(customerId)
    val user = userService.getUserById(customer.getUserId)
    map.put("allOrders", allOrders)
    map.put("tempUser", user)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/customerOrderList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteOrder/{orderId}"), method = Array(RequestMethod.GET))
  def deleteOrder(@PathVariable("orderId") orderId: Integer): String = {
    orderItemService.eraseAllByOrderId(orderId)
    orderService.deleteOrder(orderId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/getOrder/{orderId}"), method = Array(RequestMethod.GET))
  def orderDetails(@PathVariable("orderId") orderId: Integer, map: ModelMap): String = {
    val order = orderService.getOrder(orderId)
    val address = orderAddressService.getAddressById(order.getAddressId)
    val orderPrice = orderService.calculateOrderTotal(orderId)
    val orderedItems = orderItemService.listAllByOrderId(orderId)
    map.put("order", order)
    map.put("address", address)
    map.put("orderPrice", orderPrice)
    map.put("orderedItems", orderedItems)
    "fragments/orderDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allCustomers"), method = Array(RequestMethod.GET))
  def customerList(map: ModelMap): String = {
    val allCustomers = customerService.getAllCustomers
    val allUsers = userService.listAllUsers
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 6.asInstanceOf[Integer])
    "fragments/customerList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/deleteCustomer/{customerId}"), method = Array(RequestMethod.GET))
  def removeCustomer(@PathVariable("customerId") customerId: Integer): String = {
    val customer = customerService.getCustomer(customerId)
    val cart = cartService.getCartByCartId(customer.getCartId)
    val address = addressService.getAddressById(customer.getDeliveryAddressId)
    val user = userService.getUserById(customer.getUserId)
    val allOrders = orderService.listAllByCustomerId(customerId)
    for (order <- allOrders.asScala) {
      orderItemService.eraseAllByOrderId(order.getOrderId)
      orderService.deleteOrder(order.getOrderId)
    }
    cartItemService.eraseAllCartItems(cart.getCartId)
    reviewService.deleteAllByCustomerId(customerId)
    persistenceService.clearCustomerLogins(customerId)
    customerService.resetCustomer(customerId)
    val wishListIdOpt = Optional.ofNullable(customer.getWishListId)
    if (wishListIdOpt.isPresent) wishListService.deleteWishList(wishListIdOpt.get)
    val loyaltyCardIdOpt = Optional.ofNullable(customer.getLoyaltyCardId)
    if (loyaltyCardIdOpt.isPresent) loyaltyCardService.deleteLoyaltyCard(loyaltyCardIdOpt.get)
    val cardRequestOpt = Optional.ofNullable(loyaltyCardService.getRequestByCustomerId(customerId))
    if (cardRequestOpt.isPresent) loyaltyCardService.rejectRequest(cardRequestOpt.get.getId)
    addressService.deleteAddress(address.getDeliveryAddressId)
    cartService.deleteCart(cart.getCartId)
    customerService.deleteCustomer(customerId)
    userService.deleteUser(user.getId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/getCustomer/{customerId}"), method = Array(RequestMethod.GET))
  def getCustomer(@PathVariable("customerId") customerId: Integer, map: ModelMap): String = {
    val customer = customerService.getCustomer(customerId)
    val tempUser = userService.getUserById(customer.getUserId)
    val address = addressService.getAddressById(customer.getDeliveryAddressId)
    val persistence = persistenceService.getLastLogin(customerId)
    val ordersValue = orderService.calculateOrdersValue(customerId)
    map.put("tempCustomer", customer)
    map.put("tempUser", tempUser)
    map.put("address", address)
    map.put("persistence", persistence)
    map.put("ordersValue", ordersValue)
    "fragments/customerDetails :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/suspendUser/{userId}"))
  def suspendUser(@PathVariable("userId") userId: Integer): String = {
    userService.suspendUser(userId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/reactivateUser/{userId}"))
  def reactivateUser(@PathVariable("userId") userId: Integer): String = {
    userService.reactivateUser(userId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/invalidPath"), method = Array(RequestMethod.GET))
  def invalidImagePath = "fragments/invalidImagePath :: ajaxLoadedContent"

  @RequestMapping(value = Array("/genreError"), method = Array(RequestMethod.GET))
  def fireGenreExc = "fragments/genreError :: ajaxLoadedContent"
}

