package com.radovan.spring.service

import com.radovan.spring.dto.PersistenceLoginDto

trait PersistenceLoginService {

  def addPersistenceLogin(): PersistenceLoginDto

  def getLastLogin(customerId: Integer): PersistenceLoginDto
}
