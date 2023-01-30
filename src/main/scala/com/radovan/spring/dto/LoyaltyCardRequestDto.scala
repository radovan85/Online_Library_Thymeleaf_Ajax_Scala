package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class LoyaltyCardRequestDto extends Serializable {

  @BeanProperty var id:Integer = _
  @BeanProperty var customerId:Integer = _

}

