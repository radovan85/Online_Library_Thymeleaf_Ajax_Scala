package com.radovan.spring.entity


import javax.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "delivery_addresses")
@SerialVersionUID(1L)
class DeliveryAddressEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var deliveryAddressId:Integer = _

  @BeanProperty var address:String = _
  @BeanProperty var city:String = _
  @BeanProperty var state:String = _

  @Column(name = "zip_code")
  @BeanProperty var zipcode:String = _

  @BeanProperty var country:String = _

  @OneToOne(mappedBy = "deliveryAddress")
  @BeanProperty var customer:CustomerEntity = _


}

