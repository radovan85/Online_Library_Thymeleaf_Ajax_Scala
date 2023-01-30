package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class LoyaltyCardDto extends Serializable {

  @BeanProperty var loyaltyCardId:Integer = _
  @BeanProperty var discount:Integer = _
  @BeanProperty var points:Integer = _
  @BeanProperty var customerId:Integer = _


}

