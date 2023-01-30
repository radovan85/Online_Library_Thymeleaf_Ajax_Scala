package com.radovan.spring.entity

import java.util.Date
import java.util

import javax.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToMany, OneToOne, Table, Temporal, TemporalType}
import org.hibernate.annotations.{CreationTimestamp, Fetch, FetchMode}

import scala.beans.BeanProperty

@Entity
@Table(name = "customers")
@SerialVersionUID(1L)
class CustomerEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "customer_id")
  @BeanProperty var customerId:Integer = _

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "date_of_birth")
  @BeanProperty var dateOfBirth:Date = _

  @CreationTimestamp
  @Column(name = "registration_date")
  @BeanProperty var registrationDate:Date = _

  @OneToOne(cascade = Array(CascadeType.MERGE), fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  @BeanProperty var user:UserEntity = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @JoinColumn(name = "cart_id")
  @BeanProperty var cart:CartEntity = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @JoinColumn(name = "wishlist_id")
  @BeanProperty var wishList:WishListEntity = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @JoinColumn(name = "loyalty_card_id")
  @BeanProperty var loyaltyCard:LoyaltyCardEntity = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @JoinColumn(name = "delivery_address_id")
  @BeanProperty var deliveryAddress:DeliveryAddressEntity = _

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "author", orphanRemoval = true)
  @Fetch(FetchMode.SUBSELECT)
  @BeanProperty var reviews:util.List[ReviewEntity] = _

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "customer")
  @Fetch(FetchMode.SUBSELECT)
  @BeanProperty var persistenceLogins:util.List[PersistenceLoginEntity] = _


}

