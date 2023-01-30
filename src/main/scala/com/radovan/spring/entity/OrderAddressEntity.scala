package com.radovan.spring.entity



import javax.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "order_addresses")
@SerialVersionUID(1L)
class OrderAddressEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var addressId:Integer = _
  @BeanProperty var address:String = _
  @BeanProperty var city:String = _
  @BeanProperty var state:String = _

  @Column(name = "zip_code")
  @BeanProperty var zipcode:String = _
  @BeanProperty var country:String = _

  @OneToOne(mappedBy = "address", fetch = FetchType.EAGER, cascade = Array(CascadeType.ALL))
  @BeanProperty var order:OrderEntity = _


}

