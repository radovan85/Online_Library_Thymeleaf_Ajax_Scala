package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class DeliveryAddressDto extends Serializable {

  @BeanProperty var deliveryAddressId:Integer = _
  @BeanProperty var address:String = _
  @BeanProperty var city:String = _
  @BeanProperty var state:String = _
  @BeanProperty var zipcode:String = _
  @BeanProperty var country:String = _
  @BeanProperty var customerId:Integer = _


}

