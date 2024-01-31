package com.libraryapi.librarymanagement.rest

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException
import com.libraryapi.librarymanagement.exception.BusinessException
import com.libraryapi.librarymanagement.exception.EntityNotFoundException
import com.libraryapi.librarymanagement.exception.UserAlreadyExistsException
import com.libraryapi.librarymanagement.exception.ValidationException
import jakarta.validation.ConstraintViolationException
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(Exception::class)
    fun handleUncatchedException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.INTERNAL_SERVER_ERROR, request)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(ex: EntityNotFoundException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(ex: ValidationException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(ex: UserAlreadyExistsException, request: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(ex, HttpStatus.CONFLICT, request)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException, request: WebRequest): ResponseEntity<Any> {
        val errors = mutableSetOf<FieldError>()
        ex.constraintViolations.map {
            errors.add(FieldError(it.propertyPath.toString(), it.message))
        }
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, request, errors)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errors = mutableSetOf<FieldError>()
        ex.bindingResult.allErrors.map {
            when (it) {
                is org.springframework.validation.FieldError -> errors.add(FieldError(it.field, it.defaultMessage!!))
                is ObjectError -> errors.add(FieldError(it.objectName, it.defaultMessage!!))
                else -> {}
            }
        }
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, request, errors)
    }

    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errors = mutableSetOf<FieldError>()
        when (val rootCause = ex.rootCause) {
            is UnrecognizedPropertyException -> errors.add(FieldError(rootCause.propertyName, "Unrecognized field"))
            else -> {}
        }
        return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, request, errors)
    }

    private fun handleExceptionInternal(
        ex: Exception,
        status: HttpStatus,
        request: WebRequest,
        fieldErrors: Set<FieldError>? = null
    ): ResponseEntity<Any> {
        var body = super.createProblemDetail(ex, status, ex.localizedMessage, status.value().toString(), null, request)
        if (ex is ErrorResponse) body = ex.updateAndGetBody(messageSource, LocaleContextHolder.getLocale())
        fieldErrors?.let { if (it.isNotEmpty()) body.setProperty("errors", fieldErrors) }
        return super.createResponseEntity(body, HttpHeaders(), status, request)
    }

    private data class FieldError(val field: String, val error: String)
}
