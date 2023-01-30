package com.radovan.spring.dto

import java.lang.Double
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class CartItemDto extends Serializable {

  @BeanProperty var cartItemId:Integer = _
  @BeanProperty var quantity:Integer = _
  @BeanProperty var price:Double = _
  @BeanProperty var bookId:Integer = _
  @BeanProperty var cartId:Integer = _



  override def toString: String = "CartItemDto [cartItemId=" + cartItemId + ", quantity=" + quantity + ", price=" + price + ", bookId=" + bookId + ", cartId=" + cartId + "]"
}

