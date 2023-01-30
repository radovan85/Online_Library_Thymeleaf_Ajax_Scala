package com.radovan.spring.service.impl

import java.util
import java.util.Optional
import scala.collection.JavaConversions._
import java.lang.Double

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.{BookDto, WishListDto}
import com.radovan.spring.entity.{BookEntity, CartEntity, CartItemEntity, CustomerEntity, UserEntity, WishListEntity}
import com.radovan.spring.repository.{BookRepository, CartItemRepository, CartRepository, CustomerRepository, WishListRepository}
import com.radovan.spring.service.{BookService, CartItemService, CartService}
import com.radovan.spring.utils.RandomStringUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookServiceImpl extends BookService {

  @Autowired
  private var bookRepository: BookRepository = _

  @Autowired
  private var tempConverter: TempConverter = _

  @Autowired
  private var randomStringUtil: RandomStringUtil = _

  @Autowired
  private var customerRepository: CustomerRepository = _

  @Autowired
  private var wishListRepository: WishListRepository = _

  @Autowired
  private var cartRepository: CartRepository = _

  @Autowired
  private var cartItemRepository: CartItemRepository = _

  @Autowired
  private var cartService: CartService = _

  @Autowired
  private var cartItemService: CartItemService = _

  def addBook(book: BookDto): BookDto = {
    val bookId: Optional[Integer] = Optional.ofNullable(book.getBookId)
    val isbn: Optional[String] = Optional.ofNullable(book.getISBN)
    if (!isbn.isPresent) book.setISBN(randomStringUtil.getAlphaNumericString(13).toUpperCase)
    val bookEntity: BookEntity = tempConverter.bookDtoToEntity(book)
    val storedBook: BookEntity = bookRepository.save(bookEntity)
    val returnValue: BookDto = tempConverter.bookEntityToDto(storedBook)
    if (bookId.isPresent) {
      val allCartItems: Optional[util.List[CartItemEntity]] = Optional.ofNullable(cartItemRepository.findAllByBookId(returnValue.getBookId))
      if (allCartItems.isPresent) {
        for (itemEntity <- allCartItems.get) {
          var itemPrice: Double = returnValue.getPrice * itemEntity.getQuantity
          if (cartItemService.hasDiscount(itemEntity.getCartItemId)) itemPrice = itemPrice - ((itemPrice / 100) * 35)
          itemEntity.setPrice(itemPrice)
          cartItemRepository.saveAndFlush(itemEntity)
        }
        val allCarts: Optional[util.List[CartEntity]] = Optional.ofNullable(cartRepository.findAll)
        if (allCarts.isPresent) {
          for (cartEntity <- allCarts.get) {
            cartService.refreshCartState(cartEntity.getCartId)
          }
        }
      }
    }
    returnValue
  }

  def getBookById(bookId: Integer): BookDto = {
    var returnValue: BookDto = null
    val bookEntity: Optional[BookEntity] = Optional.ofNullable(bookRepository.getById(bookId))
    if (bookEntity.isPresent) returnValue = tempConverter.bookEntityToDto(bookEntity.get)
    returnValue
  }

  def getBookByISBN(isbn: String): BookDto = {
    var returnValue: BookDto = null
    val bookEntity: Optional[BookEntity] = Optional.ofNullable(bookRepository.findByISBN(isbn))
    if (bookEntity.isPresent) returnValue = tempConverter.bookEntityToDto(bookEntity.get)
    returnValue
  }

  def deleteBook(bookId: Integer): Unit = {
    bookRepository.deleteById(bookId)
    bookRepository.flush()
  }

  def listAll: util.List[BookDto] = {
    val returnValue: util.List[BookDto] = new util.ArrayList[BookDto]
    val allBooks: Optional[util.List[BookEntity]] = Optional.ofNullable(bookRepository.findAll)
    if (allBooks.isPresent) {
      for (book <- allBooks.get) {
        val bookDto: BookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  def listAllByGenreId(genreId: Integer): util.List[BookDto] = {
    val returnValue: util.List[BookDto] = new util.ArrayList[BookDto]
    val allBooks: Optional[util.List[BookEntity]] = Optional.ofNullable(bookRepository.findAllByGenreId(genreId))
    if (allBooks.isPresent) {
      for (book <- allBooks.get) {
        val bookDto: BookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  def search(keyword: String): util.List[BookDto] = {
    val returnValue: util.List[BookDto] = new util.ArrayList[BookDto]
    val allBooks: Optional[util.List[BookEntity]] = Optional.ofNullable(bookRepository.findAllByKeyword(keyword))
    if (allBooks.isPresent) {
      for (book <- allBooks.get) {
        val bookDto: BookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  def addToWishList(bookId: Integer): Unit = {
    val authUser: UserEntity = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    if (customerEntity.isPresent) {
      val customerId: Integer = customerEntity.get.getCustomerId
      val wishListEntity: Optional[WishListEntity] = Optional.ofNullable(wishListRepository.findByCustomerId(customerId))
      if (wishListEntity.isPresent) {
        val bookEntity: Optional[BookEntity] = Optional.ofNullable(bookRepository.getById(bookId))
        if (bookEntity.isPresent) {
          val wishListValue: WishListEntity = wishListEntity.get
          val bookValue: BookEntity = bookEntity.get
          val booksList: util.List[BookEntity] = wishListValue.getBooks
          val booksIds: Optional[util.List[Integer]] = Optional.ofNullable(wishListRepository.findBookIds(wishListValue.getWishListId))
          if (booksIds.isPresent) if (!booksIds.get.contains(bookValue.getBookId)) {
            booksList.add(bookValue)
            wishListValue.setBooks(booksList)
            wishListRepository.saveAndFlush(wishListValue)
          }
        }
      }
    }
  }

  def listAllFromWishList: util.List[BookDto] = {
    val authUser: UserEntity = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val returnValue: util.List[BookDto] = new util.ArrayList[BookDto]
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    if (customerEntity.isPresent) {
      val wishListEntity: Optional[WishListEntity] = Optional.ofNullable(wishListRepository.findByCustomerId(customerEntity.get.getCustomerId))
      if (wishListEntity.isPresent) {
        val allBooks: Optional[util.List[BookEntity]] = Optional.ofNullable(wishListEntity.get.getBooks)
        if (allBooks.isPresent) {
          for (book <- allBooks.get) {
            val bookDto: BookDto = tempConverter.bookEntityToDto(book)
            returnValue.add(bookDto)
          }
        }
      }
    }
    returnValue
  }

  def removeFromWishList(bookId: Integer): Unit = {
    val authUser: UserEntity = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    if (customerEntity.isPresent) {
      val wishListEntity: Optional[WishListEntity] = Optional.ofNullable(wishListRepository.findByCustomerId(customerEntity.get.getCustomerId))
      if (wishListEntity.isPresent) {
        val wishList: WishListDto = tempConverter.wishListEntityToDto(wishListEntity.get)
        val booksIds: util.List[Integer] = wishList.getBooksIds
        booksIds.remove(Integer.valueOf(bookId))
        wishList.setBooksIds(booksIds)
        val wishListValue: WishListEntity = tempConverter.wishListDtoToEntity(wishList)
        wishListRepository.saveAndFlush(wishListValue)
      }
    }
  }

  def listAllByBookId: util.List[BookDto] = {
    val returnValue: util.List[BookDto] = new util.ArrayList[BookDto]
    val allBooks: Optional[util.List[BookEntity]] = Optional.ofNullable(bookRepository.findAllSortedById)
    if (allBooks.isPresent) {
      for (book <- allBooks.get) {
        val bookDto: BookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  def listAllByRating: util.List[BookDto] = {
    val returnValue: util.List[BookDto] = new util.ArrayList[BookDto]
    val allBooks: Optional[util.List[BookEntity]] = Optional.ofNullable(bookRepository.findAllSortedByRating)
    if (allBooks.isPresent) {
      for (book <- allBooks.get) {
        val bookDto: BookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  def listAllByPrice: util.List[BookDto] = {
    val returnValue: util.List[BookDto] = new util.ArrayList[BookDto]
    val allBooks: Optional[util.List[BookEntity]] = Optional.ofNullable(bookRepository.findAllSortedByPrice)
    if (allBooks.isPresent) {
      for (book <- allBooks.get) {
        val bookDto: BookDto = tempConverter.bookEntityToDto(book)
        returnValue.add(bookDto)
      }
    }
    returnValue
  }

  def removeBookFromAllWishlist(bookId: Integer): Unit = {
    bookRepository.eraseBookFromAllWishlists(bookId)
    bookRepository.flush()
  }
}
