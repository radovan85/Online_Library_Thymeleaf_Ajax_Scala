package com.radovan.spring.dto

import java.lang.Double
import java.util

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class BookDto() extends Serializable {

  @BeanProperty var bookId:Integer = _
  @BeanProperty var ISBN:String = _
  @BeanProperty var name:String = _
  @BeanProperty var publisher:String = _
  @BeanProperty var author:String = _
  @BeanProperty var description:String = _
  @BeanProperty var language:String = _
  @BeanProperty var publishedYear:Integer = _
  @BeanProperty var pageNumber:Integer = _
  @BeanProperty var price:Double = _
  @BeanProperty var averageRating:Double = _
  @BeanProperty var imageName:String = _
  @BeanProperty var cover:String = _
  @BeanProperty var letter:String = _
  @BeanProperty var genreId:Integer = _
  @BeanProperty var reviewsIds:util.List[Integer] = _
  @BeanProperty var letters:util.LinkedHashMap[Integer,String] = _
  @BeanProperty var covers:util.LinkedHashMap[Integer,String] = _

  def getMainImagePath: String = {
    if (bookId == null || imageName == null) return "/images/bookImages/unknown.jpg"
    "/images/bookImages/" + this.imageName
  }

  letters = new util.LinkedHashMap[Integer, String]
  letters.put(1, "Han")
  letters.put(2, "Latin")
  letters.put(3, "Cyrillic")
  covers = new util.LinkedHashMap[Integer, String]
  covers.put(1, "Paperback")
  covers.put(2, "Hardcover")



  override def toString: String = "BookDto [bookId=" + bookId + ", ISBN=" + ISBN + ", name=" + name + ", publisher=" + publisher + ", author=" + author + ", description=" + description + ", language=" + language + ", publishedYear=" + publishedYear + ", pageNumber=" + pageNumber + ", price=" + price + ", averageRating=" + averageRating + ", cover=" + cover + ", letter=" + letter + ", genreId=" + genreId + "]"
}

