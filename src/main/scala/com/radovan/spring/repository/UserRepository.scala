package com.radovan.spring.repository

import java.util

import com.radovan.spring.entity.UserEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait UserRepository extends JpaRepository[UserEntity, Integer] {
  def findByEmail(email: String): UserEntity

  @Query(value = "select roles_id from users_roles where user_id = :userId", nativeQuery = true)
  def findRolesIds(@Param("userId") userId: Integer): util.List[Integer]
}
