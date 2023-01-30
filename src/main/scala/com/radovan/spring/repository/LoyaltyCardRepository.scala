package com.radovan.spring.repository

import com.radovan.spring.entity.LoyaltyCardEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait LoyaltyCardRepository extends JpaRepository[LoyaltyCardEntity,Integer]{

}
