package com.radovan.spring.entity

import java.lang.Double
import java.util.Date
import java.util

import javax.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToMany, OneToOne, Table}
import org.hibernate.annotations.CreationTimestamp

import scala.beans.BeanProperty

@Entity
@Table(name = "orders")
@SerialVersionUID(1L)
class OrderEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "order_id")
  @BeanProperty var orderId:Integer = _

  @Column(name = "order_price")
  @BeanProperty var orderPrice:Double = _

  @CreationTimestamp
  @BeanProperty var date:Date = _

  @OneToOne
  @JoinColumn(name = "customer_id")
  @BeanProperty var customer:CustomerEntity = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id")
  @BeanProperty var address:OrderAddressEntity = _

  @Column(name = "book_quantity")
  @BeanProperty var bookQuantity:Integer = _

  @BeanProperty var discount:Integer = _

  @OneToMany(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER, mappedBy = "order")
  @BeanProperty var orderedItems:util.List[OrderItemEntity] = _


}

