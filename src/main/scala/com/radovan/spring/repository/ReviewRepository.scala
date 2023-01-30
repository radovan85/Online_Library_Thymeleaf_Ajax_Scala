package com.radovan.spring.repository

import java.util

import com.radovan.spring.entity.ReviewEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait ReviewRepository extends JpaRepository[ReviewEntity, Integer] {

  @Query(value = "select * from reviews where book_id = :bookId and authorized = 1", nativeQuery = true)
  def findAllAuthorizedByBookId(@Param("bookId") bookId: Integer): util.List[ReviewEntity]

  @Query(value = "select * from reviews where customer_id = :customerId and book_id = :bookId", nativeQuery = true)
  def findByCustomerIdAndBookId(@Param("customerId") customerId: Integer, @Param("bookId") bookId: Integer): ReviewEntity

  @Query(value = "select * from reviews where authorized = 1", nativeQuery = true)
  def findAllAuthorized: util.List[ReviewEntity]

  @Query(value = "select * from reviews where authorized = 0", nativeQuery = true)
  def findAllOnHold: util.List[ReviewEntity]
}

