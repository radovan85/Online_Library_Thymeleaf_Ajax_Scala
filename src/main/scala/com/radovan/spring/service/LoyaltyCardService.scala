package com.radovan.spring.service

import java.util

import com.radovan.spring.dto.{LoyaltyCardDto, LoyaltyCardRequestDto}

trait LoyaltyCardService {

  def listAllCardRequests: util.List[LoyaltyCardRequestDto]

  def authorizeRequest(cardRequestId: Integer): Unit

  def rejectRequest(cardRequestId: Integer): Unit

  def addCardRequest(): LoyaltyCardRequestDto

  def getRequestByCustomerId(customerId: Integer): LoyaltyCardRequestDto

  def listAllLoyaltyCards: util.List[LoyaltyCardDto]

  def getCardByCardId(cardId: Integer): LoyaltyCardDto

  def updateLoyaltyCard(cardId: Integer, card: LoyaltyCardDto): LoyaltyCardDto
}
