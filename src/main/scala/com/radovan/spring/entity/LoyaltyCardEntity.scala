package com.radovan.spring.entity

import javax.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "loyalty_cards")
@SerialVersionUID(1L)
class LoyaltyCardEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "card_id")
  @BeanProperty var loyaltyCardId:Integer = _
  @BeanProperty var discount:Integer = _
  @BeanProperty var points:Integer = _

  @OneToOne
  @JoinColumn(name = "customer_id")
  @BeanProperty var customer:CustomerEntity = _


}

