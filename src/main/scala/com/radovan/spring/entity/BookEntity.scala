package com.radovan.spring.entity

import java.lang.Double
import java.util

import javax.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToMany, Table, Transient}

import scala.beans.BeanProperty


@Entity
@Table(name = "books")
@SerialVersionUID(1L)
class BookEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "book_id")
  @BeanProperty var bookId: Integer = _
  @BeanProperty var ISBN: String = _
  @BeanProperty var name: String = _
  @BeanProperty var publisher: String = _
  @BeanProperty var author: String = _
  @BeanProperty var description: String = _
  @BeanProperty var language: String = _

  @Column(name = "published_year")
  @BeanProperty var publishedYear: Integer = _

  @Column(name = "page_number")
  @BeanProperty var pageNumber: Integer = _

  @BeanProperty var price: Double = _

  @Column(name = "average_rating")
  @BeanProperty var averageRating: Double = _

  @Column(name = "image_name")
  @BeanProperty var imageName: String = _

  @BeanProperty var cover: String = _
  @BeanProperty var letter: String = _

  @ManyToOne
  @JoinColumn(name = "genre_id")
  @BeanProperty var genre: BookGenreEntity = _

  @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "book")
  @BeanProperty var reviews: util.List[ReviewEntity] = _

  @Transient
  @BeanProperty var letters: util.LinkedHashMap[Integer, String] = _

  @Transient
  @BeanProperty var covers: util.LinkedHashMap[Integer, String] = _

  @Transient
  def getMainImagePath: String = {
    if (bookId == null || imageName == null) return "/images/bookImages/unknown.jpg"
    "/images/bookImages/" + this.imageName
  }


}

