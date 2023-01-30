package com.radovan.spring.service

import java.lang.Double
import java.util

import com.radovan.spring.dto.OrderDto

trait OrderService {

  def addOrder(): OrderDto

  def listAll: util.List[OrderDto]

  def calculateOrderTotal(orderId: Integer): Double

  def getOrder(orderId: Integer): OrderDto

  def deleteOrder(orderId: Integer): Unit

  def getBookQuantity(orderId: Integer): Integer

  def refreshOrder(orderId: Integer, order: OrderDto): OrderDto

  def listAllByCustomerId(customerId: Integer): util.List[OrderDto]

  def calculateOrdersValue(customerId: Integer): Double
}
