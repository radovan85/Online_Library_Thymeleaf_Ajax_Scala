package com.radovan.spring.service.impl

import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.PersistenceLoginDto
import com.radovan.spring.entity.{PersistenceLoginEntity, UserEntity}
import com.radovan.spring.repository.{CustomerRepository, PersistenceLoginRepository}
import com.radovan.spring.service.PersistenceLoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PersistenceLoginServiceImpl extends PersistenceLoginService {

  @Autowired
  private var persistenceRepository:PersistenceLoginRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  @Autowired
  private var customerRepository:CustomerRepository = _

  override def addPersistenceLogin(): PersistenceLoginDto = {
    var returnValue:PersistenceLoginDto = null
    val authUser = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerEntity = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    if (customerEntity.isPresent) {
      val persistence = new PersistenceLoginEntity
      persistence.setCustomer(customerEntity.get)
      val storedPersistence = persistenceRepository.save(persistence)
      returnValue = tempConverter.persistenceEntityToDto(storedPersistence)
    }
    returnValue
  }

  override def getLastLogin(customerId: Integer): PersistenceLoginDto = {
    var returnValue:PersistenceLoginDto = null
    val customerEntity = Optional.ofNullable(customerRepository.getById(customerId))
    if (customerEntity.isPresent) {
      val persistence = Optional.ofNullable(persistenceRepository.findLastLogin(customerEntity.get.getCustomerId))
      if (persistence.isPresent) returnValue = tempConverter.persistenceEntityToDto(persistence.get)
    }
    returnValue
  }
}

