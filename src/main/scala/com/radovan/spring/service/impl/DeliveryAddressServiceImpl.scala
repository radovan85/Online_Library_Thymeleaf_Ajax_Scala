package com.radovan.spring.service.impl

import java.util.Optional

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.DeliveryAddressDto
import com.radovan.spring.repository.DeliveryAddressRepository
import com.radovan.spring.service.DeliveryAddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DeliveryAddressServiceImpl extends DeliveryAddressService {

  @Autowired
  private var addressRepository:DeliveryAddressRepository = _

  @Autowired
  private var tempConverter:TempConverter = _

  override def getAddressById(addressId: Integer): DeliveryAddressDto = {
    var returnValue:DeliveryAddressDto = null
    val addressEntity = Optional.ofNullable(addressRepository.getById(addressId))
    if (addressEntity.isPresent) returnValue = tempConverter.deliveryAddressEntityToDto(addressEntity.get)
    returnValue
  }

  override def createAddress(address: DeliveryAddressDto): DeliveryAddressDto = {
    val addressEntity = tempConverter.deliveryAddressDtoToEntity(address)
    val storedAddress = addressRepository.save(addressEntity)
    val returnValue = tempConverter.deliveryAddressEntityToDto(storedAddress)
    returnValue
  }
}
