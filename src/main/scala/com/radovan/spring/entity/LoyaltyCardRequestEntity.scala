package com.radovan.spring.entity

import javax.persistence.{CascadeType, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "card_requests")
@SerialVersionUID(1L)
class LoyaltyCardRequestEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @OneToOne(cascade = Array(CascadeType.MERGE), fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_id")
  @BeanProperty var customer:CustomerEntity = _


}

