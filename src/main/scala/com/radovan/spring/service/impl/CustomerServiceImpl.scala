package com.radovan.spring.service.impl

import java.util
import java.util.Optional
import scala.collection.JavaConversions._

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.{CustomerDto, DeliveryAddressDto, UserDto}
import com.radovan.spring.entity.{CartEntity, CustomerEntity, DeliveryAddressEntity, RoleEntity, UserEntity, WishListEntity}
import com.radovan.spring.exceptions.ExistingEmailException
import com.radovan.spring.model.RegistrationForm
import com.radovan.spring.repository.{CartRepository, CustomerRepository, DeliveryAddressRepository, RoleRepository, UserRepository, WishListRepository}
import com.radovan.spring.service.CustomerService
import javax.management.RuntimeErrorException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CustomerServiceImpl extends CustomerService {

  @Autowired
  private var customerRepository: CustomerRepository = _

  @Autowired
  private var tempConverter: TempConverter = _

  @Autowired
  private var cartRepository: CartRepository = _

  @Autowired
  private var userRepository: UserRepository = _

  @Autowired
  private var roleRepository: RoleRepository = _

  @Autowired
  private var wishListRepository: WishListRepository = _

  @Autowired
  private var addressRepository: DeliveryAddressRepository = _

  @Autowired
  private var passwordEncoder: BCryptPasswordEncoder = _

  override def getAllCustomers: util.List[CustomerDto] = {
    val allCustomers: Optional[util.List[CustomerEntity]] = Optional.ofNullable(customerRepository.findAll)
    val returnValue: util.List[CustomerDto] = new util.ArrayList[CustomerDto]
    if (allCustomers.isPresent) {
      for (customer <- allCustomers.get) {
        val customerDto: CustomerDto = tempConverter.customerEntityToDto(customer)
        returnValue.add(customerDto)
      }
    }
    returnValue
  }

  override def getCustomer(id: Integer): CustomerDto = {
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.getById(id))
    var returnValue: CustomerDto = new CustomerDto
    if (customerEntity.isPresent) returnValue = tempConverter.customerEntityToDto(customerEntity.get)
    else {
      val error: Error = new Error("Invalid Customer")
      throw new RuntimeErrorException(error)
    }
    returnValue
  }

  override def getCustomerByUserId(userId: Integer): CustomerDto = {
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.findByUserId(userId))
    var returnValue: CustomerDto = new CustomerDto
    if (customerEntity.isPresent) returnValue = tempConverter.customerEntityToDto(customerEntity.get)
    else {
      val error: Error = new Error("Invalid Customer")
      throw new RuntimeErrorException(error)
    }
    returnValue
  }

  override def getCustomerByCartId(cartId: Integer): CustomerDto = {
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.findByCartId(cartId))
    var returnValue: CustomerDto = new CustomerDto
    if (customerEntity.isPresent) returnValue = tempConverter.customerEntityToDto(customerEntity.get)
    else {
      val error: Error = new Error("Invalid Customer")
      throw new RuntimeErrorException(error)
    }
    returnValue
  }

  override def updateCustomer(customerId: Integer, customer: CustomerDto): CustomerDto = {
    val tempCustomer: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.getById(customerId))
    var returnValue: CustomerDto = null
    if (tempCustomer.isPresent) {
      val customerEntity: CustomerEntity = tempConverter.customerDtoToEntity(customer)
      customerEntity.setCustomerId(tempCustomer.get.getCustomerId)
      customerEntity.setCart(tempCustomer.get.getCart)
      customerEntity.setUser(tempCustomer.get.getUser)
      val updatedCustomer: CustomerEntity = customerRepository.saveAndFlush(customerEntity)
      returnValue = tempConverter.customerEntityToDto(updatedCustomer)
    }
    returnValue
  }

  override def storeCustomer(form: RegistrationForm): CustomerDto = {
    val userDto: UserDto = form.getUser
    val testUser: Optional[UserEntity] = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail))
    if (testUser.isPresent) {
      val error: Error = new Error("Email exists")
      throw new ExistingEmailException(error)
    }
    val role: RoleEntity = roleRepository.findByRole("ROLE_USER")
    userDto.setPassword(passwordEncoder.encode(userDto.getPassword))
    userDto.setEnabled(1.toByte)
    val roles: util.List[RoleEntity] = new util.ArrayList[RoleEntity]
    roles.add(role)
    val userEntity: UserEntity = tempConverter.userDtoToEntity(userDto)
    userEntity.setRoles(roles)
    userEntity.setEnabled(1.toByte)
    val storedUser: UserEntity = userRepository.save(userEntity)
    val users: util.List[UserEntity] = new util.ArrayList[UserEntity]
    users.add(storedUser)
    role.setUsers(users)
    roleRepository.saveAndFlush(role)
    val cartEntity: CartEntity = new CartEntity
    cartEntity.setCartPrice(0d)
    val storedCart: CartEntity = cartRepository.save(cartEntity)
    val wishListEntity: WishListEntity = new WishListEntity
    val storedWishList: WishListEntity = wishListRepository.save(wishListEntity)
    val deliveryAddress: DeliveryAddressDto = form.getAddress
    val deliveryAddressEntity: DeliveryAddressEntity = tempConverter.deliveryAddressDtoToEntity(deliveryAddress)
    val storedAddress: DeliveryAddressEntity = addressRepository.save(deliveryAddressEntity)
    val customerDto: CustomerDto = form.getCustomer
    customerDto.setUserId(storedUser.getId)
    customerDto.setCartId(storedCart.getCartId)
    customerDto.setWishListId(storedWishList.getWishListId)
    customerDto.setDeliveryAddressId(storedAddress.getDeliveryAddressId)
    val customerEntity: CustomerEntity = tempConverter.customerDtoToEntity(customerDto)
    val storedCustomer: CustomerEntity = customerRepository.save(customerEntity)
    storedCart.setCustomer(storedCustomer)
    cartRepository.saveAndFlush(storedCart)
    storedWishList.setCustomer(storedCustomer)
    wishListRepository.saveAndFlush(storedWishList)
    storedAddress.setCustomer(storedCustomer)
    addressRepository.saveAndFlush(storedAddress)
    val returnValue: CustomerDto = tempConverter.customerEntityToDto(storedCustomer)
    returnValue
  }

  override def addCustomer(customer: CustomerDto): CustomerDto = {
    val customerEntity: CustomerEntity = tempConverter.customerDtoToEntity(customer)
    val storedCustomer: CustomerEntity = customerRepository.save(customerEntity)
    val returnValue: CustomerDto = tempConverter.customerEntityToDto(storedCustomer)
    returnValue
  }
}

