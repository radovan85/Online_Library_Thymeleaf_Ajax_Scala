package com.radovan.spring.dto

import java.util.Date

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class PersistenceLoginDto extends Serializable {

  @BeanProperty var id:Integer = _
  @BeanProperty var date:Date = _
  @BeanProperty var customerId:Integer = _


}

