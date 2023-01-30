package com.radovan.spring.repository

import com.radovan.spring.entity.PersistenceLoginEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait PersistenceLoginRepository extends JpaRepository[PersistenceLoginEntity, Integer] {

  @Query(value = "select * from persistence_logins where customer_id = :customerId order by date desc limit 1", nativeQuery = true)
  def findLastLogin(@Param("customerId") customerId: Integer): PersistenceLoginEntity
}

