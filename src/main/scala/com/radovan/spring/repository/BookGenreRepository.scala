package com.radovan.spring.repository

import com.radovan.spring.entity.BookGenreEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait BookGenreRepository extends JpaRepository[BookGenreEntity, Integer] {

  @Query(value = "select * from genres where name = :name", nativeQuery = true)
  def findByName(@Param("name") name: String): BookGenreEntity
}
