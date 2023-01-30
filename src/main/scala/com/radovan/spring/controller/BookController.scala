package com.radovan.spring.controller

import java.util

import com.radovan.spring.dto.{BookDto, BookGenreDto, CartItemDto, CustomerDto, ReviewDto, UserDto}
import com.radovan.spring.service.{BookGenreService, BookService, CustomerService, ReviewService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{PathVariable, RequestMapping, RequestMethod, RequestParam}

@Controller
@RequestMapping(value = Array("/books"))
class BookController {

  @Autowired
  private var bookService: BookService = _

  @Autowired
  private var genreService: BookGenreService = _

  @Autowired
  private var reviewService: ReviewService = _

  @Autowired
  private var userService: UserService = _

  @Autowired
  private var customerService: CustomerService = _

  @RequestMapping(value = Array("/allBooks"))
  def allBooksList(map: ModelMap): String = {
    val allBooks: util.List[BookDto] = bookService.listAll
    map.put("allBooks", allBooks)
    "fragments/bookList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allBooksById"))
  def allBooksListSortedById(map: ModelMap): String = {
    val allBooks: util.List[BookDto] = bookService.listAllByBookId
    map.put("allBooks", allBooks)
    "fragments/bookList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allBooksByRating"))
  def allBooksListSortedByRating(map: ModelMap): String = {
    val allBooks: util.List[BookDto] = bookService.listAllByRating
    map.put("allBooks", allBooks)
    "fragments/bookList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/allBooksByPrice"))
  def allBooksListSortedByPrice(map: ModelMap): String = {
    val allBooks: util.List[BookDto] = bookService.listAllByPrice
    map.put("allBooks", allBooks)
    "fragments/bookList :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/bookDetails/{bookId}"))
  def getBookDetails(@PathVariable("bookId") bookId: Integer, map: ModelMap): String = {
    val currentBook: BookDto = bookService.getBookById(bookId)
    val allGenres: util.List[BookGenreDto] = genreService.listAll
    val allReviews: util.List[ReviewDto] = reviewService.listAllByBookId(bookId)
    val allUsers: util.List[UserDto] = userService.listAllUsers
    val allCustomers: util.List[CustomerDto] = customerService.getAllCustomers
    map.put("currentBook", currentBook)
    map.put("allGenres", allGenres)
    map.put("allReviews", allReviews)
    map.put("allUsers", allUsers)
    map.put("allCustomers", allCustomers)
    "fragments/bookDetails :: ajaxLoadedContent"
  }

  @Secured(value = Array("ROLE_USER"))
  @RequestMapping(value = Array("/addToWishList/{bookId}"), method = Array(RequestMethod.POST))
  def toWishlist(@PathVariable("bookId") bookId: Integer): String = {
    bookService.addToWishList(bookId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @Secured(value = Array("ROLE_USER"))
  @RequestMapping(value = Array("/getWishList"))
  def getWishListBooks(map: ModelMap): String = {
    val allBooks: util.List[BookDto] = bookService.listAllFromWishList
    map.put("allBooks", allBooks)
    "fragments/wishlist :: ajaxLoadedContent"
  }

  @Secured(value = Array("ROLE_USER"))
  @RequestMapping(value = Array("/deleteFromWishList/{bookId}"))
  def deleteFromWishList(@PathVariable("bookId") bookId: Integer): String = {
    bookService.removeFromWishList(bookId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @Secured(value = Array("ROLE_USER"))
  @RequestMapping(value = Array("/addToCart/{bookId}"))
  def addBookToCart(@PathVariable("bookId") bookId: Integer, map: ModelMap): String = {
    val cartItem: CartItemDto = new CartItemDto
    val selectedBook: BookDto = bookService.getBookById(bookId)
    map.put("selectedBook", selectedBook)
    map.put("cartItem", cartItem)
    "fragments/cartItemForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/searchBooks"))
  def searchBooks(@RequestParam("keyword") keyword: String, map: ModelMap): String = {
    val allBooks: util.List[BookDto] = bookService.search(keyword)
    map.put("allBooks", allBooks)
    "fragments/searchList :: ajaxLoadedContent"
  }
}

