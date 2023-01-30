package com.radovan.spring.service

import java.lang.Double
import com.radovan.spring.dto.CartDto

trait CartService {

  def getCartByCartId(cartId: Integer): CartDto

  def refreshCartState(cartId: Integer): Unit

  def calculateGrandTotal(cartId: Integer): Double

  def validateCart(cartId: Integer): CartDto

  def calculateFullPrice(cartId: Integer): Double
}
