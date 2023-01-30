package com.radovan.spring.converter

import java.text.{DateFormat, SimpleDateFormat}
import java.util
import java.util.{Date, Optional}

import scala.collection.JavaConversions._
import com.radovan.spring.dto.{BookDto, BookGenreDto, CartDto, CartItemDto, CustomerDto, DeliveryAddressDto, LoyaltyCardDto, LoyaltyCardRequestDto, OrderAddressDto, OrderDto, OrderItemDto, PersistenceLoginDto, ReviewDto, RoleDto, UserDto, WishListDto}
import com.radovan.spring.entity.{BookEntity, BookGenreEntity, CartEntity, CartItemEntity, CustomerEntity, DeliveryAddressEntity, LoyaltyCardEntity, LoyaltyCardRequestEntity, OrderAddressEntity, OrderEntity, OrderItemEntity, PersistenceLoginEntity, ReviewEntity, RoleEntity, UserEntity, WishListEntity}
import com.radovan.spring.repository.{BookGenreRepository, BookRepository, CartItemRepository, CartRepository, CustomerRepository, DeliveryAddressRepository, LoyaltyCardRepository, OrderAddressRepository, OrderItemRepository, OrderRepository, PersistenceLoginRepository, ReviewRepository, RoleRepository, UserRepository, WishListRepository}
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired

class TempConverter {

  @Autowired
  private var genreRepository: BookGenreRepository = _

  @Autowired
  private var bookRepository: BookRepository = _

  @Autowired
  private var reviewRepository: ReviewRepository = _

  @Autowired
  private var customerRepository: CustomerRepository = _

  @Autowired
  private var cartItemRepository: CartItemRepository = _

  @Autowired
  private var cartRepository: CartRepository = _

  @Autowired
  private var userRepository: UserRepository = _

  @Autowired
  private var orderItemRepository: OrderItemRepository = _

  @Autowired
  private var orderRepository: OrderRepository = _

  @Autowired
  private var roleRepository: RoleRepository = _

  @Autowired
  private var wishListRepository: WishListRepository = _

  @Autowired
  private var loyaltyCardRepository: LoyaltyCardRepository = _

  @Autowired
  private var addressRepository: DeliveryAddressRepository = _

  @Autowired
  private var persistenceRepository: PersistenceLoginRepository = _

  @Autowired
  private var orderAddressRepository: OrderAddressRepository = _

  @Autowired
  private var mapper: ModelMapper = _

  def bookEntityToDto(bookEntity: BookEntity): BookDto = {
    val returnValue: BookDto = mapper.map(bookEntity, classOf[BookDto])
    val genre: Optional[BookGenreEntity] = Optional.ofNullable(bookEntity.getGenre)
    if (genre.isPresent) returnValue.setGenreId(genre.get.getGenreId)
    val reviews: Optional[util.List[ReviewEntity]] = Optional.ofNullable(bookEntity.getReviews)
    val reviewsIds: util.List[Integer] = new util.ArrayList[Integer]
    if (reviews.isPresent) {
      for (review <- reviews.get) {
        reviewsIds.add(review.getReviewId)
      }
    }
    returnValue.setReviewsIds(reviewsIds)
    returnValue
  }

  def bookDtoToEntity(book: BookDto): BookEntity = {
    val returnValue: BookEntity = mapper.map(book, classOf[BookEntity])
    val genreId: Optional[Integer] = Optional.ofNullable(book.getGenreId)
    if (genreId.isPresent) {
      val genre: BookGenreEntity = genreRepository.getById(genreId.get)
      returnValue.setGenre(genre)
    }
    val reviewsIds: Optional[util.List[Integer]] = Optional.ofNullable(book.getReviewsIds)
    val reviews: util.List[ReviewEntity] = new util.ArrayList[ReviewEntity]
    if (reviewsIds.isPresent) {
      for (reviewId <- reviewsIds.get) {
        val review: ReviewEntity = reviewRepository.getById(reviewId)
        reviews.add(review)
      }
    }
    returnValue.setReviews(reviews)
    returnValue
  }

  def bookGenreEntityToDto(genreEntity: BookGenreEntity): BookGenreDto = {
    val returnValue: BookGenreDto = mapper.map(genreEntity, classOf[BookGenreDto])
    val books: Optional[util.List[BookEntity]] = Optional.ofNullable(genreEntity.getBooks)
    val booksIds: util.List[Integer] = new util.ArrayList[Integer]
    if (books.isPresent) {
      for (book <- books.get) {
        booksIds.add(book.getBookId)
      }
    }
    returnValue.setBooksIds(booksIds)
    returnValue
  }

  def bookGenreDtoToEntity(genre: BookGenreDto): BookGenreEntity = {
    val returnValue: BookGenreEntity = mapper.map(genre, classOf[BookGenreEntity])
    val booksIds: Optional[util.List[Integer]] = Optional.ofNullable(genre.getBooksIds)
    val books: util.List[BookEntity] = new util.ArrayList[BookEntity]
    if (booksIds.isPresent) {
      for (bookId <- booksIds.get) {
        val bookEntity: BookEntity = bookRepository.getById(bookId)
        books.add(bookEntity)
      }
    }
    returnValue.setBooks(books)
    returnValue
  }

  def cartEntityToDto(cartEntity: CartEntity): CartDto = {
    val returnValue: CartDto = mapper.map(cartEntity, classOf[CartDto])
    val cartPrice: Optional[Double] = Optional.ofNullable(cartEntity.getCartPrice)
    if (!cartPrice.isPresent) returnValue.setCartPrice(0d)
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(cartEntity.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    val itemsIds: util.List[Integer] = new util.ArrayList[Integer]
    val cartItems: Optional[util.List[CartItemEntity]] = Optional.ofNullable(cartEntity.getCartItems)
    if (cartItems.isPresent) {
      for (itemEntity <- cartItems.get) {
        val itemId: Integer = itemEntity.getCartItemId
        itemsIds.add(itemId)
      }
    }
    returnValue.setCartItemsIds(itemsIds)
    returnValue
  }

  def cartDtoToEntity(cartDto: CartDto): CartEntity = {
    val returnValue: CartEntity = mapper.map(cartDto, classOf[CartEntity])
    val cartPrice: Optional[Double] = Optional.ofNullable(cartDto.getCartPrice)
    if (!cartPrice.isPresent) returnValue.setCartPrice(0d)
    val customerId: Optional[Integer] = Optional.ofNullable(cartDto.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity: CustomerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    val cartItems: util.List[CartItemEntity] = new util.ArrayList[CartItemEntity]
    val itemIds: Optional[util.List[Integer]] = Optional.ofNullable(cartDto.getCartItemsIds)
    if (itemIds.isPresent) {
      for (itemId <- itemIds.get) {
        val itemEntity: CartItemEntity = cartItemRepository.getById(itemId)
        cartItems.add(itemEntity)
      }
    }
    returnValue.setCartItems(cartItems)
    returnValue
  }

  def cartItemEntityToDto(cartItemEntity: CartItemEntity): CartItemDto = {
    val returnValue: CartItemDto = mapper.map(cartItemEntity, classOf[CartItemDto])
    val book: Optional[BookEntity] = Optional.ofNullable(cartItemEntity.getBook)
    if (book.isPresent) returnValue.setBookId(book.get.getBookId)
    val cart: Optional[CartEntity] = Optional.ofNullable(cartItemEntity.getCart)
    if (cart.isPresent) returnValue.setCartId(cart.get.getCartId)
    returnValue
  }

  def cartItemDtoToEntity(cartItemDto: CartItemDto): CartItemEntity = {
    val returnValue: CartItemEntity = mapper.map(cartItemDto, classOf[CartItemEntity])
    val cartId: Optional[Integer] = Optional.ofNullable(cartItemDto.getCartId)
    if (cartId.isPresent) {
      val cartEntity: CartEntity = cartRepository.getById(cartId.get)
      returnValue.setCart(cartEntity)
    }
    val bookId: Optional[Integer] = Optional.ofNullable(cartItemDto.getBookId)
    if (bookId.isPresent) {
      val bookEntity: BookEntity = bookRepository.getById(bookId.get)
      returnValue.setBook(bookEntity)
    }
    returnValue
  }

  def customerEntityToDto(customerEntity: CustomerEntity): CustomerDto = {
    val returnValue: CustomerDto = mapper.map(customerEntity, classOf[CustomerDto])
    val userEntity: Optional[UserEntity] = Optional.ofNullable(customerEntity.getUser)
    if (userEntity.isPresent) returnValue.setUserId(userEntity.get.getId)
    val cartEntity: Optional[CartEntity] = Optional.ofNullable(customerEntity.getCart)
    if (cartEntity.isPresent) returnValue.setCartId(cartEntity.get.getCartId)
    val wishListEntity: Optional[WishListEntity] = Optional.ofNullable(customerEntity.getWishList)
    if (wishListEntity.isPresent) returnValue.setWishListId(wishListEntity.get.getWishListId)
    val loyaltyCard: Optional[LoyaltyCardEntity] = Optional.ofNullable(customerEntity.getLoyaltyCard)
    if (loyaltyCard.isPresent) returnValue.setLoyaltyCardId(loyaltyCard.get.getLoyaltyCardId)
    val address: Optional[DeliveryAddressEntity] = Optional.ofNullable(customerEntity.getDeliveryAddress)
    if (address.isPresent) returnValue.setDeliveryAddressId(address.get.getDeliveryAddressId)
    val reviews: Optional[util.List[ReviewEntity]] = Optional.ofNullable(customerEntity.getReviews)
    val reviewsIds: util.List[Integer] = new util.ArrayList[Integer]
    if (reviews.isPresent) {
      for (review <- reviews.get) {
        reviewsIds.add(review.getReviewId)
      }
    }
    returnValue.setReviewsIds(reviewsIds)
    val persistenceLoginsIds: util.List[Integer] = new util.ArrayList[Integer]
    val persistenceLogins: Optional[util.List[PersistenceLoginEntity]] = Optional.ofNullable(customerEntity.getPersistenceLogins)
    if (persistenceLogins.isPresent) {
      for (persistence <- persistenceLogins.get) {
        persistenceLoginsIds.add(persistence.getId)
      }
    }
    returnValue.setPersistenceLoginsIds(persistenceLoginsIds)
    returnValue
  }

  def customerDtoToEntity(customer: CustomerDto): CustomerEntity = {
    val returnValue: CustomerEntity = mapper.map(customer, classOf[CustomerEntity])
    val userId: Optional[Integer] = Optional.ofNullable(customer.getUserId)
    if (userId.isPresent) {
      val userEntity: UserEntity = userRepository.getById(userId.get)
      returnValue.setUser(userEntity)
    }
    val cartId: Optional[Integer] = Optional.ofNullable(customer.getCartId)
    if (cartId.isPresent) {
      val cartEntity: CartEntity = cartRepository.getById(cartId.get)
      returnValue.setCart(cartEntity)
    }
    val wishListId: Optional[Integer] = Optional.ofNullable(customer.getWishListId)
    if (wishListId.isPresent) {
      val wishListEntity: WishListEntity = wishListRepository.getById(wishListId.get)
      returnValue.setWishList(wishListEntity)
    }
    val loyaltyCardId: Optional[Integer] = Optional.ofNullable(customer.getLoyaltyCardId)
    if (loyaltyCardId.isPresent) {
      val cardEntity: LoyaltyCardEntity = loyaltyCardRepository.getById(loyaltyCardId.get)
      returnValue.setLoyaltyCard(cardEntity)
    }
    val delieryAddressId: Optional[Integer] = Optional.ofNullable(customer.getDeliveryAddressId)
    if (delieryAddressId.isPresent) {
      val addressEntity: DeliveryAddressEntity = addressRepository.getById(delieryAddressId.get)
      returnValue.setDeliveryAddress(addressEntity)
    }
    val reviewsIds: Optional[util.List[Integer]] = Optional.ofNullable(customer.getReviewsIds)
    val reviews: util.List[ReviewEntity] = new util.ArrayList[ReviewEntity]
    if (reviewsIds.isPresent) {
      for (reviewId <- reviewsIds.get) {
        val reviewEntity: ReviewEntity = reviewRepository.getById(reviewId)
        reviews.add(reviewEntity)
      }
    }
    returnValue.setReviews(reviews)
    val persistenceLoginsIds: Optional[util.List[Integer]] = Optional.ofNullable(customer.getPersistenceLoginsIds)
    val persistenceLogins: util.List[PersistenceLoginEntity] = new util.ArrayList[PersistenceLoginEntity]
    if (persistenceLoginsIds.isPresent) {
      for (persistenceId <- persistenceLoginsIds.get) {
        val persistence: PersistenceLoginEntity = persistenceRepository.getById(persistenceId)
        persistenceLogins.add(persistence)
      }
    }
    returnValue.setPersistenceLogins(persistenceLogins)
    val dateOfBirthStr: Optional[String] = Optional.ofNullable(customer.getDateOfBirthStr)
    if (dateOfBirthStr.isPresent) try {
      val formatter: DateFormat = new SimpleDateFormat("yyyy-MM-dd")
      val date: Date = formatter.parse(dateOfBirthStr.get)
      returnValue.setDateOfBirth(date)
    } catch {
      case ex: Exception =>
        ex.printStackTrace()
    }
    returnValue
  }

  def loyaltyCardEntityToDto(card: LoyaltyCardEntity): LoyaltyCardDto = {
    val returnValue: LoyaltyCardDto = mapper.map(card, classOf[LoyaltyCardDto])
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(card.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    returnValue
  }

  def loyaltyCardDtoToEntity(card: LoyaltyCardDto): LoyaltyCardEntity = {
    val returnValue: LoyaltyCardEntity = mapper.map(card, classOf[LoyaltyCardEntity])
    val customerId: Optional[Integer] = Optional.ofNullable(card.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity: CustomerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def orderEntityToDto(orderEntity: OrderEntity): OrderDto = {
    val returnValue: OrderDto = mapper.map(orderEntity, classOf[OrderDto])
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(orderEntity.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    val addressEntity: Optional[OrderAddressEntity] = Optional.ofNullable(orderEntity.getAddress)
    if (addressEntity.isPresent) returnValue.setAddressId(addressEntity.get.getAddressId)
    val orderedItems: Optional[util.List[OrderItemEntity]] = Optional.ofNullable(orderEntity.getOrderedItems)
    val orderedItemsIds: util.List[Integer] = new util.ArrayList[Integer]
    if (orderedItems.isPresent) {
      for (item <- orderedItems.get) {
        orderedItemsIds.add(item.getOrderItemId)
      }
    }
    returnValue.setOrderedItemsIds(orderedItemsIds)
    returnValue
  }

  def orderDtoToEntity(order: OrderDto): OrderEntity = {
    val returnValue: OrderEntity = mapper.map(order, classOf[OrderEntity])
    val customerId: Optional[Integer] = Optional.ofNullable(order.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity: CustomerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    val addressId: Optional[Integer] = Optional.ofNullable(order.getAddressId)
    if (addressId.isPresent) {
      val addressEntity: OrderAddressEntity = orderAddressRepository.getById(addressId.get)
      returnValue.setAddress(addressEntity)
    }
    val orderedItemsIds: Optional[util.List[Integer]] = Optional.ofNullable(order.getOrderedItemsIds)
    val orderedItems: util.List[OrderItemEntity] = new util.ArrayList[OrderItemEntity]
    if (orderedItemsIds.isPresent) {
      for (itemId <- orderedItemsIds.get) {
        val item: OrderItemEntity = orderItemRepository.getById(itemId)
        orderedItems.add(item)
      }
    }
    returnValue.setOrderedItems(orderedItems)
    returnValue
  }

  def orderItemEntityToDto(itemEntity: OrderItemEntity): OrderItemDto = {
    val returnValue: OrderItemDto = mapper.map(itemEntity, classOf[OrderItemDto])
    val orderEntity: Optional[OrderEntity] = Optional.ofNullable(itemEntity.getOrder)
    if (orderEntity.isPresent) returnValue.setOrderId(orderEntity.get.getOrderId)
    returnValue
  }

  def orderItemDtoToEntity(itemDto: OrderItemDto): OrderItemEntity = {
    val returnValue: OrderItemEntity = mapper.map(itemDto, classOf[OrderItemEntity])
    val orderId: Optional[Integer] = Optional.ofNullable(itemDto.getOrderId)
    if (orderId.isPresent) {
      val orderEntity: OrderEntity = orderRepository.getById(orderId.get)
      returnValue.setOrder(orderEntity)
    }
    returnValue
  }

  def reviewEntityToDto(reviewEntity: ReviewEntity): ReviewDto = {
    val returnValue: ReviewDto = mapper.map(reviewEntity, classOf[ReviewDto])
    val author: Optional[CustomerEntity] = Optional.ofNullable(reviewEntity.getAuthor)
    if (author.isPresent) returnValue.setAuthorId(author.get.getCustomerId)
    val bookEntity: Optional[BookEntity] = Optional.ofNullable(reviewEntity.getBook)
    if (bookEntity.isPresent) returnValue.setBookId(bookEntity.get.getBookId)
    returnValue
  }

  def reviewDtoToEntity(review: ReviewDto): ReviewEntity = {
    val returnValue: ReviewEntity = mapper.map(review, classOf[ReviewEntity])
    val authorId: Optional[Integer] = Optional.ofNullable(review.getAuthorId)
    if (authorId.isPresent) {
      val author: CustomerEntity = customerRepository.getById(authorId.get)
      returnValue.setAuthor(author)
    }
    val bookId: Optional[Integer] = Optional.ofNullable(review.getBookId)
    if (bookId.isPresent) {
      val bookEntity: BookEntity = bookRepository.getById(bookId.get)
      returnValue.setBook(bookEntity)
    }
    returnValue
  }

  def wishListEntityToDto(wishList: WishListEntity): WishListDto = {
    val returnValue: WishListDto = mapper.map(wishList, classOf[WishListDto])
    val books: Optional[util.List[BookEntity]] = Optional.ofNullable(wishList.getBooks)
    val booksIds: util.List[Integer] = new util.ArrayList[Integer]
    if (books.isPresent) {
      for (book <- books.get) {
        booksIds.add(book.getBookId)
      }
    }
    returnValue.setBooksIds(booksIds)
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(wishList.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    returnValue
  }

  def wishListDtoToEntity(wishList: WishListDto): WishListEntity = {
    val returnValue: WishListEntity = mapper.map(wishList, classOf[WishListEntity])
    val booksIds: Optional[util.List[Integer]] = Optional.ofNullable(wishList.getBooksIds)
    val books: util.List[BookEntity] = new util.ArrayList[BookEntity]
    if (booksIds.isPresent) {
      for (bookId <- booksIds.get) {
        val bookEntity: BookEntity = bookRepository.getById(bookId)
        books.add(bookEntity)
      }
    }
    returnValue.setBooks(books)
    val customerId: Optional[Integer] = Optional.ofNullable(wishList.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity: CustomerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def cardRequestEntityToDto(request: LoyaltyCardRequestEntity): LoyaltyCardRequestDto = {
    val returnValue: LoyaltyCardRequestDto = mapper.map(request, classOf[LoyaltyCardRequestDto])
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(request.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    returnValue
  }

  def cardRequestDtoToEntity(request: LoyaltyCardRequestDto): LoyaltyCardRequestEntity = {
    val returnValue: LoyaltyCardRequestEntity = mapper.map(request, classOf[LoyaltyCardRequestEntity])
    val customerId: Optional[Integer] = Optional.ofNullable(request.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity: CustomerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def deliveryAddressEntityToDto(address: DeliveryAddressEntity): DeliveryAddressDto = {
    val returnValue: DeliveryAddressDto = mapper.map(address, classOf[DeliveryAddressDto])
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(address.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    returnValue
  }

  def deliveryAddressDtoToEntity(address: DeliveryAddressDto): DeliveryAddressEntity = {
    val returnValue: DeliveryAddressEntity = mapper.map(address, classOf[DeliveryAddressEntity])
    val customerId: Optional[Integer] = Optional.ofNullable(address.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity: CustomerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def cartItemToOrderItemEntity(cartItemEntity: CartItemEntity): OrderItemEntity = {
    val returnValue: OrderItemEntity = mapper.map(cartItemEntity, classOf[OrderItemEntity])
    val book: Optional[BookEntity] = Optional.ofNullable(cartItemEntity.getBook)
    if (book.isPresent) {
      returnValue.setBookName(book.get.getName)
      returnValue.setBookPrice(book.get.getPrice)
    }
    returnValue
  }

  def persistenceEntityToDto(persistence: PersistenceLoginEntity): PersistenceLoginDto = {
    val returnValue: PersistenceLoginDto = mapper.map(persistence, classOf[PersistenceLoginDto])
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(persistence.getCustomer)
    if (customerEntity.isPresent) returnValue.setCustomerId(customerEntity.get.getCustomerId)
    returnValue
  }

  def persistenceDtoToEntity(persistence: PersistenceLoginDto): PersistenceLoginEntity = {
    val returnValue: PersistenceLoginEntity = mapper.map(persistence, classOf[PersistenceLoginEntity])
    val customerId: Optional[Integer] = Optional.ofNullable(persistence.getCustomerId)
    if (customerId.isPresent) {
      val customerEntity: CustomerEntity = customerRepository.getById(customerId.get)
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def orderAddressEntityToDto(address: OrderAddressEntity): OrderAddressDto = {
    val returnValue: OrderAddressDto = mapper.map(address, classOf[OrderAddressDto])
    val orderEntity: Optional[OrderEntity] = Optional.ofNullable(address.getOrder)
    if (orderEntity.isPresent) returnValue.setOrderId(orderEntity.get.getOrderId)
    returnValue
  }

  def orderAddressDtoToEntity(address: OrderAddressDto): OrderAddressEntity = {
    val returnValue: OrderAddressEntity = mapper.map(address, classOf[OrderAddressEntity])
    val orderId: Optional[Integer] = Optional.ofNullable(address.getOrderId)
    if (orderId.isPresent) {
      val orderEntity: OrderEntity = orderRepository.getById(orderId.get)
      returnValue.setOrder(orderEntity)
    }
    returnValue
  }

  def addressToOrderAddress(address: DeliveryAddressEntity): OrderAddressEntity = {
    val returnValue: OrderAddressEntity = mapper.map(address, classOf[OrderAddressEntity])
    returnValue
  }

  def userEntityToDto(userEntity: UserEntity): UserDto = {
    val returnValue: UserDto = mapper.map(userEntity, classOf[UserDto])
    returnValue.setEnabled(userEntity.getEnabled)
    val roles: Optional[util.List[RoleEntity]] = Optional.ofNullable(userEntity.getRoles)
    val rolesIds: util.List[Integer] = new util.ArrayList[Integer]
    if (roles.isPresent) {
      for (roleEntity <- roles.get) {
        rolesIds.add(roleEntity.getId)
      }
    }
    returnValue.setRolesIds(rolesIds)
    returnValue
  }

  def userDtoToEntity(userDto: UserDto): UserEntity = {
    val returnValue: UserEntity = mapper.map(userDto, classOf[UserEntity])
    val roles: util.List[RoleEntity] = new util.ArrayList[RoleEntity]
    val rolesIds: Optional[util.List[Integer]] = Optional.ofNullable(userDto.getRolesIds)
    if (rolesIds.isPresent) {
      for (roleId <- rolesIds.get) {
        val role: RoleEntity = roleRepository.getById(roleId)
        roles.add(role)
      }
    }
    returnValue.setRoles(roles)
    returnValue
  }

  def roleEntityToDto(roleEntity: RoleEntity): RoleDto = {
    val returnValue: RoleDto = mapper.map(roleEntity, classOf[RoleDto])
    val users: Optional[util.List[UserEntity]] = Optional.ofNullable(roleEntity.getUsers)
    val userIds:util.List[Integer]  = new util.ArrayList[Integer]
    if (users.isPresent) {
      for (user <- users.get) {
        userIds.add(user.getId)
      }
    }
    returnValue.setUsersIds(userIds)
    returnValue
  }

  def roleDtoToEntity(roleDto: RoleDto): RoleEntity = {
    val returnValue: RoleEntity = mapper.map(roleDto, classOf[RoleEntity])
    val usersIds: Optional[util.List[Integer]] = Optional.ofNullable(roleDto.getUsersIds)
    val users: util.List[UserEntity] = new util.ArrayList[UserEntity]
    if (usersIds.isPresent) {
      for (userId <- usersIds.get) {
        val userEntity: UserEntity = userRepository.getById(userId)
        users.add(userEntity)
      }
    }
    returnValue.setUsers(users)
    returnValue
  }
}

