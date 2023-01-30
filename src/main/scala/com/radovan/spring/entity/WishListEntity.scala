package com.radovan.spring.entity

import javax.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, JoinTable, ManyToMany, OneToOne, Table}
import org.hibernate.annotations.{Fetch, FetchMode}

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "wishlists")
@SerialVersionUID(1L)
class WishListEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "wishlist_id")
  @BeanProperty var wishListId:Integer = _

  @ManyToMany(fetch = FetchType.EAGER, cascade = Array(CascadeType.MERGE))
  @JoinTable(name = "books_wishlists", joinColumns = Array(new JoinColumn(name = "wishlist_id")), inverseJoinColumns = Array(new JoinColumn(name = "book_id")))
  @Fetch(FetchMode.SUBSELECT)
  @BeanProperty var books:util.List[BookEntity] = _

  @OneToOne
  @JoinColumn(name = "customer_id")
  @BeanProperty var customer:CustomerEntity = _


}

