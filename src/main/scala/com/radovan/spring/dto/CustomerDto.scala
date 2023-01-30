package com.radovan.spring.dto

import java.util.Date
import java.util
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class CustomerDto extends Serializable {

  @BeanProperty var customerId:Integer = _
  @BeanProperty var dateOfBirth:Date = _
  @BeanProperty var dateOfBirthStr:String = _
  @BeanProperty var registrationDate:Date = _
  @BeanProperty var userId:Integer = _
  @BeanProperty var cartId:Integer = _
  @BeanProperty var loyaltyCardId:Integer = _
  @BeanProperty var deliveryAddressId:Integer = _
  @BeanProperty var reviewsIds:util.List[Integer] = _
  @BeanProperty var wishListId:Integer = _
  @BeanProperty var persistenceLoginsIds:util.List[Integer] = _


}

