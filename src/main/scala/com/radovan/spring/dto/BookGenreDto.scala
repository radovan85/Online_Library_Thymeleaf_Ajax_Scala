package com.radovan.spring.dto

import java.util
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class BookGenreDto extends Serializable {

  @BeanProperty var genreId:Integer = _
  @BeanProperty var name:String = _
  @BeanProperty var description:String = _
  @BeanProperty var booksIds:util.List[Integer] = _


}

