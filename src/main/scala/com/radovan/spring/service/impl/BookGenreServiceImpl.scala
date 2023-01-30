package com.radovan.spring.service.impl

import java.util
import java.util.Optional
import scala.collection.JavaConversions._

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.BookGenreDto
import com.radovan.spring.entity.{BookEntity, BookGenreEntity}
import com.radovan.spring.exceptions.DeleteGenreException
import com.radovan.spring.repository.{BookGenreRepository, BookRepository}
import com.radovan.spring.service.BookGenreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookGenreServiceImpl extends BookGenreService {

  @Autowired
  private var genreRepository: BookGenreRepository = _

  @Autowired
  private var tempConverter: TempConverter = _

  @Autowired
  private var bookRepository: BookRepository = _

  override def addGenre(genre: BookGenreDto): BookGenreDto = {
    val genreEntity: BookGenreEntity = tempConverter.bookGenreDtoToEntity(genre)
    val storedGenre: BookGenreEntity = genreRepository.save(genreEntity)
    val returnValue: BookGenreDto = tempConverter.bookGenreEntityToDto(storedGenre)
    returnValue
  }

  override def getGenreById(genreId: Integer): BookGenreDto = {
    var returnValue: BookGenreDto = null
    val genreEntity: Optional[BookGenreEntity] = Optional.ofNullable(genreRepository.getById(genreId))
    if (genreEntity.isPresent) returnValue = tempConverter.bookGenreEntityToDto(genreEntity.get)
    returnValue
  }

  override def getGenreByName(name: String): BookGenreDto = {
    var returnValue: BookGenreDto = null
    val genreEntity: Optional[BookGenreEntity] = Optional.ofNullable(genreRepository.findByName(name))
    if (genreEntity.isPresent) returnValue = tempConverter.bookGenreEntityToDto(genreEntity.get)
    returnValue
  }

  override def deleteGenre(genreId: Integer): Unit = {
    val books: util.List[BookEntity] = bookRepository.findAllByGenreId(genreId)
    if (!books.isEmpty) {
      val error: Error = new Error("Genre contains books")
      throw new DeleteGenreException(error)
    }
    genreRepository.deleteById(genreId)
    genreRepository.flush()
  }

  override def listAll: util.List[BookGenreDto] = {
    val returnValue: util.List[BookGenreDto] = new util.ArrayList[BookGenreDto]
    val allGenres: Optional[util.List[BookGenreEntity]] = Optional.ofNullable(genreRepository.findAll)
    if (allGenres.isPresent) {
      for (genre <- allGenres.get) {
        val genreDto: BookGenreDto = tempConverter.bookGenreEntityToDto(genre)
        returnValue.add(genreDto)
      }
    }
    returnValue
  }
}

