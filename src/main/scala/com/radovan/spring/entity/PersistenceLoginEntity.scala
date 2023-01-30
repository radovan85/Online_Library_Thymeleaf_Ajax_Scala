package com.radovan.spring.entity

import java.util.Date
import javax.persistence.{Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}
import org.hibernate.annotations.CreationTimestamp

import scala.beans.BeanProperty

@Entity
@Table(name = "persistence_logins")
@SerialVersionUID(1L)
class PersistenceLoginEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var id:Integer = _

  @CreationTimestamp
  @BeanProperty var date:Date = _

  @ManyToOne
  @JoinColumn(name = "customer_id")
  @BeanProperty var customer:CustomerEntity = _


}

