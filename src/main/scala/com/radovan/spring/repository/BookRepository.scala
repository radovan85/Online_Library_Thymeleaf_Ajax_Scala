package com.radovan.spring.repository

import java.lang.Double
import java.util

import com.radovan.spring.entity.BookEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait BookRepository extends JpaRepository[BookEntity, Integer] {

  @Query(value = "select * from books where isbn = :isbn", nativeQuery = true)
  def findByISBN(@Param("isbn") isbn: String): BookEntity

  @Query(value = "select * from books where genre_id = :genreId", nativeQuery = true)
  def findAllByGenreId(@Param("genreId") genreId: Integer): util.List[BookEntity]

  @Query(value = "select * from books where isbn ilike CONCAT('%', :keyword, '%')" + " or publisher ilike CONCAT('%' ,:keyword, '%')" + " or author ilike CONCAT('%', :keyword, '%')" + " or description ilike CONCAT('%', :keyword, '%')" + " or name ilike CONCAT('%', :keyword, '%')", nativeQuery = true)
  def findAllByKeyword(@Param("keyword") keyword: String): util.List[BookEntity]

  @Query(value = "select cast(avg(rating) as decimal(4,2)) from reviews where book_id = :bookId and authorized=1", nativeQuery = true)
  def calculateAverageRating(@Param("bookId") bookId: Integer): Double

  @Query(value = "select * from books order by book_id asc", nativeQuery = true)
  def findAllSortedById: util.List[BookEntity]

  @Query(value = "select * from books order by average_rating desc", nativeQuery = true)
  def findAllSortedByRating: util.List[BookEntity]

  @Query(value = "select * from books order by price asc", nativeQuery = true)
  def findAllSortedByPrice: util.List[BookEntity]

  @Modifying
  @Query(value = "delete from books_wishlists where book_id = :bookId", nativeQuery = true)
  def eraseBookFromAllWishlists(@Param("bookId") bookId: Integer): Unit
}

