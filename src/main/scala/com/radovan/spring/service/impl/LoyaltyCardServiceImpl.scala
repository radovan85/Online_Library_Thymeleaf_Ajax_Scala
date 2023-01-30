package com.radovan.spring.service.impl

import java.util
import java.util.Optional
import scala.collection.JavaConversions._

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.{LoyaltyCardDto, LoyaltyCardRequestDto}
import com.radovan.spring.entity.{CustomerEntity, LoyaltyCardEntity, LoyaltyCardRequestEntity, UserEntity}
import com.radovan.spring.repository.{CustomerRepository, LoyaltyCardRepository, LoyaltyCardRequestRepository}
import com.radovan.spring.service.LoyaltyCardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class LoyaltyCardServiceImpl extends LoyaltyCardService {

  @Autowired
  private var requestRepository: LoyaltyCardRequestRepository = _

  @Autowired
  private var cardRepository: LoyaltyCardRepository = _

  @Autowired
  private var customerRepository: CustomerRepository = _

  @Autowired
  private var tempConverter: TempConverter = _

  override def listAllCardRequests: util.List[LoyaltyCardRequestDto] = {
    val returnValue: util.List[LoyaltyCardRequestDto] = new util.ArrayList[LoyaltyCardRequestDto]
    val allRequests: Optional[util.List[LoyaltyCardRequestEntity]] = Optional.ofNullable(requestRepository.findAll)
    if (allRequests.isPresent) {
      for (request <- allRequests.get) {
        val requestDto: LoyaltyCardRequestDto = tempConverter.cardRequestEntityToDto(request)
        returnValue.add(requestDto)
      }
    }
    returnValue
  }

  override def authorizeRequest(cardRequestId: Integer): Unit = {
    val cardRequest: Optional[LoyaltyCardRequestEntity] = Optional.ofNullable(requestRepository.getById(cardRequestId))
    if (cardRequest.isPresent) {
      val cardEntity: LoyaltyCardEntity = new LoyaltyCardEntity
      cardEntity.setDiscount(0)
      cardEntity.setPoints(0)
      val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(cardRequest.get.getCustomer)
      if (customerEntity.isPresent) {
        val customerValue: CustomerEntity = customerEntity.get
        val storedCard: LoyaltyCardEntity = cardRepository.save(cardEntity)
        customerValue.setLoyaltyCard(storedCard)
        val updatedCustomer: CustomerEntity = customerRepository.saveAndFlush(customerValue)
        storedCard.setCustomer(updatedCustomer)
        cardRepository.saveAndFlush(storedCard)
        requestRepository.deleteById(cardRequestId)
        requestRepository.flush()
      }
    }
  }

  override def rejectRequest(cardRequestId: Integer): Unit = {
    requestRepository.deleteById(cardRequestId)
    requestRepository.flush()
  }

  override def addCardRequest(): LoyaltyCardRequestDto = {
    var returnValue: LoyaltyCardRequestDto = null
    val authUser: UserEntity = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    if (customerEntity.isPresent) {
      val cardRequestEntity: LoyaltyCardRequestEntity = new LoyaltyCardRequestEntity
      cardRequestEntity.setCustomer(customerEntity.get)
      val storedRequest: LoyaltyCardRequestEntity = requestRepository.save(cardRequestEntity)
      returnValue = tempConverter.cardRequestEntityToDto(storedRequest)
    }
    returnValue
  }

  override def getRequestByCustomerId(customerId: Integer): LoyaltyCardRequestDto = {
    var returnValue: LoyaltyCardRequestDto = null
    val cardRequest: Optional[LoyaltyCardRequestEntity] = Optional.ofNullable(requestRepository.findByCustomerId(customerId))
    if (cardRequest.isPresent) returnValue = tempConverter.cardRequestEntityToDto(cardRequest.get)
    returnValue
  }

  override def listAllLoyaltyCards: util.List[LoyaltyCardDto] = {
    val returnValue: util.List[LoyaltyCardDto] = new util.ArrayList[LoyaltyCardDto]
    val allCards: Optional[util.List[LoyaltyCardEntity]] = Optional.ofNullable(cardRepository.findAll)
    if (allCards.isPresent) {
      for (card <- allCards.get) {
        val cardDto: LoyaltyCardDto = tempConverter.loyaltyCardEntityToDto(card)
        returnValue.add(cardDto)
      }
    }
    returnValue
  }

  override def getCardByCardId(cardId: Integer): LoyaltyCardDto = {
    var returnValue: LoyaltyCardDto = null
    val cardEntity: Optional[LoyaltyCardEntity] = Optional.ofNullable(cardRepository.getById(cardId))
    if (cardEntity.isPresent) returnValue = tempConverter.loyaltyCardEntityToDto(cardEntity.get)
    returnValue
  }

  override def updateLoyaltyCard(cardId: Integer, card: LoyaltyCardDto): LoyaltyCardDto = {
    val cardEntity: LoyaltyCardEntity = tempConverter.loyaltyCardDtoToEntity(card)
    cardEntity.setLoyaltyCardId(cardId)
    val updatedCard: LoyaltyCardEntity = cardRepository.saveAndFlush(cardEntity)
    val returnValue: LoyaltyCardDto = tempConverter.loyaltyCardEntityToDto(updatedCard)
    returnValue
  }
}

