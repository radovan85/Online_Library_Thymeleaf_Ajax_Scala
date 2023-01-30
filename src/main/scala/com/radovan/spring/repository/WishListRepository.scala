package com.radovan.spring.repository

import java.util

import com.radovan.spring.entity.WishListEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait WishListRepository extends JpaRepository[WishListEntity, Integer] {

  @Query(value = "select * from wishlists where customer_id = :customerId", nativeQuery = true)
  def findByCustomerId(@Param("customerId") customerId: Integer): WishListEntity

  @Query(value = "select book_id from books_wishlists where wishlist_id = :wishlistId", nativeQuery = true)
  def findBookIds(@Param("wishlistId") wishlistId: Integer): util.List[Integer]
}
