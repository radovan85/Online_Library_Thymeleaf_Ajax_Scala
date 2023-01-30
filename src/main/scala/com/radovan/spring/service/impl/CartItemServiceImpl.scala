package com.radovan.spring.service.impl

import java.util
import java.util.Optional
import scala.collection.JavaConversions._

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.entity.{CartEntity, CartItemEntity, CustomerEntity, LoyaltyCardEntity}
import com.radovan.spring.repository.{CartItemRepository, CartRepository}
import com.radovan.spring.service.{CartItemService, CartService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CartItemServiceImpl extends CartItemService {

  @Autowired
  private var cartItemRepository: CartItemRepository = _

  @Autowired
  private var tempConverter: TempConverter = _

  @Autowired
  private var cartService: CartService = _

  @Autowired
  private var cartRepository: CartRepository = _

  override def addCartItem(cartItem: CartItemDto): CartItemDto = {
    val cartItemEntity: CartItemEntity = tempConverter.cartItemDtoToEntity(cartItem)
    val storedItem: CartItemEntity = cartItemRepository.save(cartItemEntity)
    val returnValue: CartItemDto = tempConverter.cartItemEntityToDto(storedItem)
    returnValue
  }

  override def removeCartItem(cartId: Integer, itemId: Integer): Unit = {
    cartItemRepository.removeCartItem(cartId, itemId)
    cartItemRepository.flush()
    cartService.refreshCartState(cartId)
  }

  override def eraseAllCartItems(cartId: Integer): Unit = {
    cartItemRepository.removeAllByCartId(cartId)
    cartItemRepository.flush()
    cartService.refreshCartState(cartId)
  }

  override def listAllByCartId(cartId: Integer): util.List[CartItemDto] = {
    val cartItems: Optional[util.List[CartItemEntity]] = Optional.ofNullable(cartItemRepository.findAllByCartId(cartId))
    val returnValue: util.List[CartItemDto] = new util.ArrayList[CartItemDto]
    if (cartItems.isPresent) {
      for (item <- cartItems.get) {
        val itemDto: CartItemDto = tempConverter.cartItemEntityToDto(item)
        returnValue.add(itemDto)
      }
    }
    returnValue
  }

  override def getCartItem(id: Integer): CartItemDto = {
    val cartItemEntity: Optional[CartItemEntity] = Optional.ofNullable(cartItemRepository.getById(id))
    var returnValue: CartItemDto = null
    if (cartItemEntity.isPresent) returnValue = tempConverter.cartItemEntityToDto(cartItemEntity.get)
    returnValue
  }

  override def getCartItemByCartIdAndBookId(cartId: Integer, bookId: Integer): CartItemDto = {
    val cartItemEntity: Optional[CartItemEntity] = Optional.ofNullable(cartItemRepository.findByCartIdAndBookId(cartId, bookId))
    var returnValue: CartItemDto = null
    if (cartItemEntity.isPresent) returnValue = tempConverter.cartItemEntityToDto(cartItemEntity.get)
    returnValue
  }

  override def getBookQuantity(cartId: Integer): Integer = {
    var returnValue: Integer = 0
    val bookQuantity: Optional[Integer] = Optional.ofNullable(cartItemRepository.findBookQuantity(cartId))
    if (bookQuantity.isPresent) returnValue = bookQuantity.get
    returnValue
  }

  override def eraseAllByBookId(bookId: Integer): Unit = {
    cartItemRepository.removeAllByBookId(bookId)
    cartItemRepository.flush()
    val allCarts: Optional[util.List[CartEntity]] = Optional.ofNullable(cartRepository.findAll)
    if (allCarts.isPresent) {
      for (cartEntity <- allCarts.get) {
        cartService.refreshCartState(cartEntity.getCartId)
      }
    }
  }

  override def hasDiscount(itemId: Integer): Boolean = {
    var returnValue: Boolean = false
    val itemEntity: Optional[CartItemEntity] = Optional.ofNullable(cartItemRepository.getById(itemId))
    if (itemEntity.isPresent) {
      val cartEntity: CartEntity = itemEntity.get.getCart
      val customerEntity: CustomerEntity = cartEntity.getCustomer
      val loyaltyCard: Optional[LoyaltyCardEntity] = Optional.ofNullable(customerEntity.getLoyaltyCard)
      if (loyaltyCard.isPresent) {
        val discount: Integer = loyaltyCard.get.getDiscount
        if (discount > 0) returnValue = true
      }
    }
    returnValue
  }
}

