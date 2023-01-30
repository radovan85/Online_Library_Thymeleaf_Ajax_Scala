package com.radovan.spring.dto

import java.lang.Double
import java.util
import java.util.Date
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class OrderDto extends Serializable {

  @BeanProperty var orderId:Integer = _
  @BeanProperty var orderPrice:Double = _
  @BeanProperty var date:Date = _
  @BeanProperty var customerId:Integer = _
  @BeanProperty var addressId:Integer = _
  @BeanProperty var bookQuantity:Integer = _
  @BeanProperty var discount:Integer = _
  @BeanProperty var purchasedBooksIds:util.List[Integer] = _
  @BeanProperty var orderedItemsIds:util.List[Integer] = _


}

