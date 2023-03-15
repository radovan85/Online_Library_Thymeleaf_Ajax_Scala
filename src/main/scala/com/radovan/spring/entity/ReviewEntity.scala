package com.radovan.spring.entity

import java.sql.Timestamp

import javax.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "reviews")
@SerialVersionUID(1L)
class ReviewEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "review_id")
  @BeanProperty var reviewId:Integer = _

  @BeanProperty var text:String = _
  @BeanProperty var rating:Integer = _
  @BeanProperty var createdAt:Timestamp = _

  @ManyToOne
  @JoinColumn(name = "customer_id")
  @BeanProperty var author:CustomerEntity = _

  @ManyToOne
  @JoinColumn(name = "book_id")
  @BeanProperty var book:BookEntity = _

  @BeanProperty var authorized:Byte = _


}
