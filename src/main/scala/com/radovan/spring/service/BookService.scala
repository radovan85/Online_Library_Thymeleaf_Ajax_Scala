package com.radovan.spring.service

import java.util

import com.radovan.spring.dto.BookDto

trait BookService {

  def addBook(book: BookDto): BookDto

  def getBookById(bookId: Integer): BookDto

  def getBookByISBN(isbn: String): BookDto

  def deleteBook(bookId: Integer): Unit

  def listAll: util.List[BookDto]

  def listAllByGenreId(genreId: Integer): util.List[BookDto]

  def search(keyword: String): util.List[BookDto]

  def addToWishList(bookId: Integer): Unit

  def listAllFromWishList: util.List[BookDto]

  def removeFromWishList(bookId: Integer): Unit

  def listAllByBookId: util.List[BookDto]

  def listAllByRating: util.List[BookDto]

  def listAllByPrice: util.List[BookDto]

  def removeBookFromAllWishlist(bookId: Integer): Unit
}

