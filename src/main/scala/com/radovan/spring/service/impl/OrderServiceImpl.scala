package com.radovan.spring.service.impl

import java.lang.Double
import java.util
import java.util.Optional
import scala.collection.JavaConversions._

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderDto
import com.radovan.spring.entity.{CartEntity, CartItemEntity, CustomerEntity, DeliveryAddressEntity, OrderAddressEntity, OrderEntity, OrderItemEntity, UserEntity}
import com.radovan.spring.repository.{CartItemRepository, CustomerRepository, OrderAddressRepository, OrderItemRepository, OrderRepository}
import com.radovan.spring.service.{CartService, OrderService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderServiceImpl extends OrderService {

  @Autowired
  private var orderRepository: OrderRepository = _

  @Autowired
  private var orderItemRepository: OrderItemRepository = _

  @Autowired
  private var cartItemRepository: CartItemRepository = _

  @Autowired
  private var customerRepository: CustomerRepository = _

  @Autowired
  private var tempConverter: TempConverter = _

  @Autowired
  private var cartService: CartService = _

  @Autowired
  private var orderAddressRepository: OrderAddressRepository = _

  override def addOrder(): OrderDto = {
    var returnValue: OrderDto = null
    val authUser: UserEntity = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerOptional: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    var customerEntity: CustomerEntity = null
    val orderEntity: OrderEntity = new OrderEntity
    var orderedItems: util.List[OrderItemEntity] = new util.ArrayList[OrderItemEntity]
    if (customerOptional.isPresent) {
      customerEntity = customerOptional.get
      val cartEntityOptional: Optional[CartEntity] = Optional.ofNullable(customerEntity.getCart)
      if (cartEntityOptional.isPresent) {
        val cartEntity: CartEntity = cartEntityOptional.get
        val allCartItems: Optional[util.List[CartItemEntity]] = Optional.ofNullable(cartEntity.getCartItems)
        if (allCartItems.isPresent) {
          for (cartItem <- allCartItems.get) {
            val orderItem: OrderItemEntity = tempConverter.cartItemToOrderItemEntity(cartItem)
            orderedItems.add(orderItem)
          }
          cartItemRepository.removeAllByCartId(cartEntity.getCartId)
          cartService.refreshCartState(cartEntity.getCartId)
          val deliveryAddress: DeliveryAddressEntity = customerEntity.getDeliveryAddress
          val orderAddress: OrderAddressEntity = tempConverter.addressToOrderAddress(deliveryAddress)
          val storedOrderAddress: OrderAddressEntity = orderAddressRepository.save(orderAddress)
          orderEntity.setCustomer(customerEntity)
          orderEntity.setAddress(storedOrderAddress)
          val discount: Optional[Integer] = Optional.ofNullable(orderEntity.getDiscount)
          if (!discount.isPresent) orderEntity.setDiscount(0)
          var storedOrder: OrderEntity = orderRepository.save(orderEntity)
          for (orderItem <- orderedItems) {
            orderItem.setOrder(storedOrder)
            orderItemRepository.save(orderItem)
          }
          orderedItems = orderItemRepository.findAllByOrderId(storedOrder.getOrderId)
          val orderPrice: Optional[Double] = Optional.ofNullable(orderItemRepository.calculateGrandTotal(storedOrder.getOrderId))
          if (orderPrice.isPresent) storedOrder.setOrderPrice(orderPrice.get)
          storedOrderAddress.setOrder(storedOrder)
          orderAddressRepository.saveAndFlush(storedOrderAddress)
          val bookQuantity: Integer = getBookQuantity(storedOrder.getOrderId)
          storedOrder.setBookQuantity(bookQuantity)
          storedOrder.setOrderedItems(orderedItems)
          storedOrder = orderRepository.saveAndFlush(storedOrder)
          returnValue = tempConverter.orderEntityToDto(storedOrder)
        }
      }
    }
    returnValue
  }

  override def listAll: util.List[OrderDto] = {
    val returnValue: util.List[OrderDto] = new util.ArrayList[OrderDto]
    val allOrders: Optional[util.List[OrderEntity]] = Optional.ofNullable(orderRepository.findAll)
    if (allOrders.isPresent) {
      for (order <- allOrders.get) {
        val orderDto: OrderDto = tempConverter.orderEntityToDto(order)
        returnValue.add(orderDto)
      }
    }
    returnValue
  }

  override def calculateOrderTotal(orderId: Integer): Double = {
    var returnValue: Double = null
    val orderTotal: Optional[Double] = Optional.ofNullable(orderItemRepository.calculateGrandTotal(orderId))
    if (orderTotal.isPresent) returnValue = orderTotal.get
    returnValue
  }

  override def getOrder(orderId: Integer): OrderDto = {
    var returnValue: OrderDto = null
    val orderEntity: Optional[OrderEntity] = Optional.ofNullable(orderRepository.getById(orderId))
    if (orderEntity.isPresent) returnValue = tempConverter.orderEntityToDto(orderEntity.get)
    returnValue
  }

  override def deleteOrder(orderId: Integer): Unit = {
    orderRepository.deleteById(orderId)
    orderRepository.flush()
  }

  override def getBookQuantity(orderId: Integer): Integer = {
    var returnValue: Integer = null
    val bookQuantity: Optional[Integer] = Optional.ofNullable(orderItemRepository.findBookQuantity(orderId))
    if (bookQuantity.isPresent) returnValue = bookQuantity.get
    returnValue
  }

  override def refreshOrder(orderId: Integer, order: OrderDto): OrderDto = {
    val orderEntity: OrderEntity = tempConverter.orderDtoToEntity(order)
    orderEntity.setOrderId(orderId)
    val storedOrder: OrderEntity = orderRepository.saveAndFlush(orderEntity)
    val returnValue: OrderDto = tempConverter.orderEntityToDto(storedOrder)
    returnValue
  }

  override def listAllByCustomerId(customerId: Integer): util.List[OrderDto] = {
    val returnValue: util.List[OrderDto] = new util.ArrayList[OrderDto]
    val allOrders: Optional[util.List[OrderEntity]] = Optional.ofNullable(orderRepository.findAllByCustomerId(customerId))
    if (allOrders.isPresent) {
      for (order <- allOrders.get) {
        val orderDto: OrderDto = tempConverter.orderEntityToDto(order)
        returnValue.add(orderDto)
      }
    }
    returnValue
  }

  override def calculateOrdersValue(customerId: Integer): Double = {
    var returnValue: Double = 0d
    val ordersAmount: Optional[Double] = Optional.ofNullable(orderRepository.getOrdersValue(customerId))
    if (ordersAmount.isPresent) returnValue = ordersAmount.get
    returnValue
  }
}

