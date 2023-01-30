package com.radovan.spring.repository

import java.lang.Double
import java.util

import com.radovan.spring.entity.OrderEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait OrderRepository extends JpaRepository[OrderEntity, Integer] {

  @Query(value = "select * from orders where customer_id = :customerId", nativeQuery = true)
  def findAllByCustomerId(@Param("customerId") customerId: Integer): util.List[OrderEntity]

  @Query(value = "select sum(order_price) from orders where customer_id = :customerId", nativeQuery = true)
  def getOrdersValue(@Param("customerId") customerId: Integer): Double
}

