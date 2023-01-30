package com.radovan.spring.service.impl

import java.util
import java.util.Optional
import scala.collection.JavaConversions._

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.UserDto
import com.radovan.spring.entity.{RoleEntity, UserEntity}
import com.radovan.spring.exceptions.{ExistingEmailException, InvalidUserException}
import com.radovan.spring.repository.{RoleRepository, UserRepository}
import com.radovan.spring.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl extends UserService {

  @Autowired
  private var userRepository: UserRepository = _

  @Autowired
  private var roleRepository: RoleRepository = _

  @Autowired
  private var passwordEncoder: BCryptPasswordEncoder = _

  @Autowired
  private var tempConverter: TempConverter = _

  override def updateUser(id: Integer, user: UserDto): UserDto = {
    val tempUser: Optional[UserEntity] = Optional.ofNullable(userRepository.getById(id))
    var returnValue: UserDto = null
    if (tempUser.isPresent) {
      val userEntity: UserEntity = tempConverter.userDtoToEntity(user)
      userEntity.setEnabled(tempUser.get.getEnabled)
      userEntity.setRoles(tempUser.get.getRoles)
      userEntity.setId(tempUser.get.getId)
      val updatedUser: UserEntity = userRepository.saveAndFlush(userEntity)
      returnValue = tempConverter.userEntityToDto(updatedUser)
    }
    else {
      val error: Error = new Error("Invalid user")
      throw new InvalidUserException(error)
    }
    returnValue
  }

  override def deleteUser(id: Integer): Unit = {
    userRepository.deleteById(id)
    userRepository.flush()
  }

  override def getUserById(id: Integer): UserDto = {
    val userEntity: Optional[UserEntity] = Optional.ofNullable(userRepository.getById(id))
    var returnValue: UserDto = null
    if (userEntity.isPresent) returnValue = tempConverter.userEntityToDto(userEntity.get)
    else {
      val error: Error = new Error("Invalid User")
      throw new InvalidUserException(error)
    }
    returnValue
  }

  override def listAllUsers: util.List[UserDto] = {
    val allUsers: Optional[util.List[UserEntity]] = Optional.ofNullable(userRepository.findAll)
    val returnValue: util.List[UserDto] = new util.ArrayList[UserDto]
    if (allUsers.isPresent) {
      for (userEntity <- allUsers.get) {
        val userDto: UserDto = tempConverter.userEntityToDto(userEntity)
        returnValue.add(userDto)
      }
    }
    returnValue
  }

  override def getUserByEmail(email: String): UserEntity = {
    val userEntity: Optional[UserEntity] = Optional.ofNullable(userRepository.findByEmail(email))
    var returnValue: UserEntity = null
    if (userEntity.isPresent) returnValue = userEntity.get
    else {
      val error: Error = new Error("Invalid User")
      throw new InvalidUserException(error)
    }
    returnValue
  }

  override def storeUser(user: UserDto): UserDto = {
    val testUser: Optional[UserEntity] = Optional.ofNullable(userRepository.findByEmail(user.getEmail))
    if (testUser.isPresent) {
      val error: Error = new Error("Email exists")
      throw new ExistingEmailException(error)
    }
    val role: RoleEntity = roleRepository.findByRole("ROLE_USER")
    user.setPassword(passwordEncoder.encode(user.getPassword))
    user.setEnabled(1.toByte)
    val roles: util.List[RoleEntity] = new util.ArrayList[RoleEntity]
    roles.add(role)
    val userEntity: UserEntity = tempConverter.userDtoToEntity(user)
    userEntity.setRoles(roles)
    userEntity.setEnabled(1.toByte)
    val storedUser: UserEntity = userRepository.save(userEntity)
    val users: util.List[UserEntity] = new util.ArrayList[UserEntity]
    users.add(storedUser)
    role.setUsers(users)
    roleRepository.saveAndFlush(role)
    val returnValue: UserDto = tempConverter.userEntityToDto(storedUser)
    returnValue
  }

  override def getCurrentUser: UserDto = {
    val authUser: UserEntity = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val userEntity: UserEntity = userRepository.getById(authUser.getId)
    val returnValue: UserDto = tempConverter.userEntityToDto(userEntity)
    returnValue
  }

  override def suspendUser(userId: Integer): Unit = {
    val userEntity: UserEntity = userRepository.getById(userId)
    userEntity.setEnabled(0.toByte)
    userRepository.saveAndFlush(userEntity)
  }

  override def reactivateUser(userId: Integer): Unit = {
    val user: Optional[UserEntity] = Optional.ofNullable(userRepository.getById(userId))
    if (user.isPresent) {
      val userEntity: UserEntity = user.get
      userEntity.setEnabled(1.toByte)
      userRepository.saveAndFlush(userEntity)
    }
  }
}

