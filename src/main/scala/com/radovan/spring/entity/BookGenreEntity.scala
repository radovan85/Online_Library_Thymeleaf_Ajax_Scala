package com.radovan.spring.entity

import javax.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, OneToMany, Table}

import scala.beans.BeanProperty
import java.util

@Entity
@Table(name = "genres")
@SerialVersionUID(1L)
class BookGenreEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "genre_id")
  @BeanProperty var genreId:Integer = _
  @BeanProperty var name:String = _
  @BeanProperty var description:String = _

  @OneToMany(mappedBy = "genre", fetch = FetchType.EAGER, orphanRemoval = true)
  @BeanProperty var books:util.List[BookEntity] = _


}

