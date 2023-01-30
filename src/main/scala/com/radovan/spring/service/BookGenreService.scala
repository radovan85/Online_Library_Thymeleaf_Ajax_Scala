package com.radovan.spring.service

import java.util

import com.radovan.spring.dto.BookGenreDto

trait BookGenreService {

  def addGenre(genre: BookGenreDto): BookGenreDto

  def getGenreById(genreId: Integer): BookGenreDto

  def getGenreByName(name: String): BookGenreDto

  def deleteGenre(genreId: Integer): Unit

  def listAll: util.List[BookGenreDto]
}
