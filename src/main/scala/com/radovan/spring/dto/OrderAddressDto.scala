package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class OrderAddressDto extends Serializable {

  @BeanProperty var addressId:Integer = _
  @BeanProperty var address:String = _
  @BeanProperty var city:String = _
  @BeanProperty var state:String = _
  @BeanProperty var zipcode:String = _
  @BeanProperty var country:String = _
  @BeanProperty var orderId:Integer = _


}

