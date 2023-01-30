package com.radovan.spring.entity

import java.lang.Double

import javax.persistence.{CascadeType, Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "cart_items")
@SerialVersionUID(1L)
class CartItemEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var cartItemId:Integer = _

  @BeanProperty var quantity:Integer = _

  @BeanProperty var price:Double = _

  @ManyToOne(cascade = Array(CascadeType.ALL))
  @JoinColumn(name = "book_id")
  @BeanProperty var book:BookEntity = _

  @ManyToOne
  @JoinColumn(name = "cart_id")
  @BeanProperty var cart:CartEntity = _


}

