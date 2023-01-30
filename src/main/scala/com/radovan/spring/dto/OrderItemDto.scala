package com.radovan.spring.dto

import java.lang.Double
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class OrderItemDto extends Serializable {

  @BeanProperty var orderItemId:Integer = _
  @BeanProperty var quantity:Integer = _
  @BeanProperty var price:Double = _
  @BeanProperty var bookName:String = _
  @BeanProperty var bookPrice:Double = _
  @BeanProperty var orderId:Integer = _


}

