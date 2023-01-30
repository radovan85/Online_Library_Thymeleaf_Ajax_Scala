package com.radovan.spring.controller

import java.util

import com.radovan.spring.dto.{BookDto, ReviewDto}
import com.radovan.spring.service.{BookService, ReviewService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{ModelAttribute, PathVariable, RequestMapping, RequestMethod}

@Controller
@RequestMapping(value = Array("/reviews"))
class ReviewController {

  @Autowired
  private var reviewService: ReviewService = _

  @Autowired
  private var bookService: BookService = _

  @RequestMapping(value = Array("/createReview/{bookId}"))
  def renderReviewForm(@PathVariable("bookId") bookId: Integer, map: ModelMap): String = {
    val review: ReviewDto = new ReviewDto
    val book: BookDto = bookService.getBookById(bookId)
    val ratings: util.List[Integer] = new util.ArrayList[Integer]
    var x: Int = 1
    while ( {
      x <= 5
    }) {
      ratings.add(x)

      {
        x += 1; x - 1
      }
    }
    map.put("review", review)
    map.put("book", book)
    map.put("ratings", ratings)
    "fragments/reviewForm :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/createReview"), method = Array(RequestMethod.POST))
  def createReview(@ModelAttribute("review") review: ReviewDto): String = {
    reviewService.addReview(review)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @RequestMapping(value = Array("/reviewSentCompleted"))
  def reviewCompleted: String = "fragments/reviewSent :: ajaxLoadedContent"
}

