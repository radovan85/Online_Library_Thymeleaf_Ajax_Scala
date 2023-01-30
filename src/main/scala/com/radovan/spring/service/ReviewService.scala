package com.radovan.spring.service

import java.util

import com.radovan.spring.dto.ReviewDto

trait ReviewService {

  def addReview(review: ReviewDto): ReviewDto

  def getReviewById(reviewId: Integer): ReviewDto

  def listAll: util.List[ReviewDto]

  def listAllByBookId(bookId: Integer): util.List[ReviewDto]

  def listAllAuthorized: util.List[ReviewDto]

  def listAllOnHold: util.List[ReviewDto]

  def deleteReview(reviewId: Integer): Unit

  def authorizeReview(reviewId: Integer): Unit
}
