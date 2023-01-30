package com.radovan.spring.model

import com.radovan.spring.dto.{CustomerDto, DeliveryAddressDto, UserDto}

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class RegistrationForm extends Serializable {

  @BeanProperty var user:UserDto = _
  @BeanProperty var customer:CustomerDto = _
  @BeanProperty var address:DeliveryAddressDto = _


}

