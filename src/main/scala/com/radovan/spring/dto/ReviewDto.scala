package com.radovan.spring.dto

import java.util.Date

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class ReviewDto extends Serializable {

  @BeanProperty var reviewId:Integer = _
  @BeanProperty var text:String = _
  @BeanProperty var rating:Integer = _
  @BeanProperty var date:Date = _
  @BeanProperty var authorId:Integer = _
  @BeanProperty var bookId:Integer = _
  @BeanProperty var authorized:Byte = _


  override def toString: String = "ReviewDto [reviewId=" + reviewId + ", text=" + text + ", rating=" + rating + ", date=" + date + ", authorId=" + authorId + ", bookId=" + bookId + ", authorized=" + authorized + "]"
}

