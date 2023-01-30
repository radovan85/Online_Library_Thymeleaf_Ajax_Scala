package com.radovan.spring.repository

import com.radovan.spring.entity.DeliveryAddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait DeliveryAddressRepository extends JpaRepository[DeliveryAddressEntity,Integer]{

}
