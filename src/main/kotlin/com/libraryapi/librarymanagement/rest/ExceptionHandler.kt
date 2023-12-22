package com.libraryapi.librarymanagement.rest

import com.libraryapi.librarymanagement.exception.BookAlreadyExistsException
import com.libraryapi.librarymanagement.exception.BookNotFoundException
import com.libraryapi.librarymanagement.exception.ConversionIdException
import com.libraryapi.librarymanagement.exception.UserAlreadyExistsException
import com.libraryapi.librarymanagement.exception.UserNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleUncatchedException(e: Exception, request: WebRequest): ResponseEntity<Any>? {
        return handleExceptionInternal(e, HttpStatus.INTERNAL_SERVER_ERROR, request)
    }

    @ExceptionHandler(BookNotFoundException::class)
    fun handleBookNotFoundException(e: BookNotFoundException, request: WebRequest): ResponseEntity<Any>? {
        return handleExceptionInternal(e, HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(BookAlreadyExistsException::class)
    fun handleBookAlreadyExistsException(e: BookAlreadyExistsException, request: WebRequest): ResponseEntity<Any>? {
        return handleExceptionInternal(e, HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(ConversionIdException::class)
    fun handleConversionIdException(e: ConversionIdException, request: WebRequest): ResponseEntity<Any>? {
        return handleExceptionInternal(e, HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(e: UserAlreadyExistsException, request: WebRequest): ResponseEntity<Any>? {
        return handleExceptionInternal(e, HttpStatus.CONFLICT, request)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(e: UserNotFoundException, request: WebRequest): ResponseEntity<Any>? {
        return handleExceptionInternal(e, HttpStatus.NOT_FOUND, request)
    }

    private fun handleExceptionInternal(ex: Exception, status: HttpStatus, request: WebRequest): ResponseEntity<Any>? {
        val body = super.createProblemDetail(ex, status, ex.localizedMessage, status.value().toString(), null, request)
        return super.handleExceptionInternal(ex, body, HttpHeaders(), status, request)
    }
}
