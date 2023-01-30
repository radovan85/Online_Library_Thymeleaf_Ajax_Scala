package com.radovan.spring.service.impl

import java.util
import java.util.Optional
import scala.collection.JavaConversions._

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.ReviewDto
import com.radovan.spring.entity.{BookEntity, CustomerEntity, ReviewEntity, UserEntity}
import com.radovan.spring.exceptions.ReviewAlreadyExistsException
import com.radovan.spring.repository.{BookRepository, CustomerRepository, ReviewRepository}
import com.radovan.spring.service.ReviewService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewServiceImpl extends ReviewService {

  @Autowired
  private var reviewRepository: ReviewRepository = _

  @Autowired
  private var tempConverter: TempConverter = _

  @Autowired
  private var customerRepository: CustomerRepository = _

  @Autowired
  private var bookRepository: BookRepository = _

  override def addReview(review: ReviewDto): ReviewDto = {
    val authUser: UserEntity = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerEntity: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    if (customerEntity.isPresent) review.setAuthorId(customerEntity.get.getCustomerId)
    val existingReview: Optional[ReviewEntity] = Optional.ofNullable(reviewRepository.findByCustomerIdAndBookId(customerEntity.get.getCustomerId, review.getBookId))
    if (existingReview.isPresent) {
      val error: Error = new Error("Existing Review")
      throw new ReviewAlreadyExistsException(error)
    }
    review.setAuthorized(0.toByte)
    val reviewEntity: ReviewEntity = tempConverter.reviewDtoToEntity(review)
    val storedReview: ReviewEntity = reviewRepository.save(reviewEntity)
    val returnValue: ReviewDto = tempConverter.reviewEntityToDto(storedReview)
    returnValue
  }

  override def getReviewById(reviewId: Integer): ReviewDto = {
    var returnValue: ReviewDto = null
    val reviewEntity: Optional[ReviewEntity] = Optional.ofNullable(reviewRepository.getById(reviewId))
    if (reviewEntity.isPresent) returnValue = tempConverter.reviewEntityToDto(reviewEntity.get)
    returnValue
  }

  override def listAll: util.List[ReviewDto] = {
    val returnValue: util.List[ReviewDto] = new util.ArrayList[ReviewDto]
    val allReviews: Optional[util.List[ReviewEntity]] = Optional.ofNullable(reviewRepository.findAll)
    if (allReviews.isPresent) {
      for (review <- allReviews.get) {
        val reviewDto: ReviewDto = tempConverter.reviewEntityToDto(review)
        returnValue.add(reviewDto)
      }
    }
    returnValue
  }

  override def listAllByBookId(bookId: Integer): util.List[ReviewDto] = {
    val returnValue: util.List[ReviewDto] = new util.ArrayList[ReviewDto]
    val allReviews: Optional[util.List[ReviewEntity]] = Optional.ofNullable(reviewRepository.findAllAuthorizedByBookId(bookId))
    if (allReviews.isPresent) {
      for (review <- allReviews.get) {
        val reviewDto: ReviewDto = tempConverter.reviewEntityToDto(review)
        returnValue.add(reviewDto)
      }
    }
    returnValue
  }

  override def deleteReview(reviewId: Integer): Unit = {
    reviewRepository.deleteById(reviewId)
    reviewRepository.flush()
  }

  override def listAllAuthorized: util.List[ReviewDto] = {
    val returnValue: util.List[ReviewDto] = new util.ArrayList[ReviewDto]
    val allReviews: Optional[util.List[ReviewEntity]] = Optional.ofNullable(reviewRepository.findAllAuthorized)
    if (allReviews.isPresent) {
      for (review <- allReviews.get) {
        val reviewDto: ReviewDto = tempConverter.reviewEntityToDto(review)
        returnValue.add(reviewDto)
      }
    }
    returnValue
  }

  override def listAllOnHold: util.List[ReviewDto] = {
    val returnValue: util.List[ReviewDto] = new util.ArrayList[ReviewDto]
    val allReviews: Optional[util.List[ReviewEntity]] = Optional.ofNullable(reviewRepository.findAllOnHold)
    if (allReviews.isPresent) {
      for (review <- allReviews.get) {
        val reviewDto: ReviewDto = tempConverter.reviewEntityToDto(review)
        returnValue.add(reviewDto)
      }
    }
    returnValue
  }

  override def authorizeReview(reviewId: Integer): Unit = {
    val reviewEntity: Optional[ReviewEntity] = Optional.ofNullable(reviewRepository.getById(reviewId))
    if (reviewEntity.isPresent) {
      val reviewValue: ReviewEntity = reviewEntity.get
      reviewValue.setAuthorized(1.toByte)
      reviewRepository.saveAndFlush(reviewValue)
      val bookEntity: Optional[BookEntity] = Optional.ofNullable(reviewValue.getBook)
      if (bookEntity.isPresent) {
        val bookValue: BookEntity = bookEntity.get
        val avgRating: Optional[Double] = Optional.ofNullable(bookRepository.calculateAverageRating(bookValue.getBookId))
        if (avgRating.isPresent) {
          bookValue.setAverageRating(avgRating.get)
          bookRepository.saveAndFlush(bookValue)
        }
      }
    }
  }
}

