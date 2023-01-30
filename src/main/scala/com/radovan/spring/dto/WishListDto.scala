package com.radovan.spring.dto

import java.util
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class WishListDto extends Serializable {

  @BeanProperty var wishListId:Integer = _
  @BeanProperty var booksIds:util.List[Integer] = _
  @BeanProperty var customerId:Integer = _
}
