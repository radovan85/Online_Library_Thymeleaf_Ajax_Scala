package com.radovan.spring.repository

import com.radovan.spring.entity.LoyaltyCardRequestEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait LoyaltyCardRequestRepository extends JpaRepository[LoyaltyCardRequestEntity, Integer] {

  @Query(value = "select * from card_requests where customer_id = :customerId", nativeQuery = true)
  def findByCustomerId(@Param("customerId") customerId: Integer): LoyaltyCardRequestEntity
}
