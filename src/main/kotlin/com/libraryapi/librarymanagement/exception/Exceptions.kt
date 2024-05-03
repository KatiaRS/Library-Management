package com.libraryapi.librarymanagement.exception
open class EntityNotFoundException(message: String) : Exception(message)
open class BusinessException(message: String?) : Exception(message)
open class ValidationException(message: String) : Exception(message)
class BookNotFoundException : EntityNotFoundException("Book not found")
class BookAlreadyExistsException : BusinessException("This book already exists")
class UserAlreadyExistsException : BusinessException("This user already exists")
class UserNotFoundException : EntityNotFoundException("User not found")
class ConversionIdException : ValidationException("Invalid Id")
class DocumentCannotBeChangedException : BusinessException("Document cannot be changed")
class UserReachedLoanLimitException : BusinessException("Loan limit reached by user")
class NoCopiesAvailableException : BusinessException("No books available")
class LoanNotFoundException : EntityNotFoundException("Loan not found")
class BookAlreadyBeenReturnedException : BusinessException("This book has already been returned")
class FineNotFoundException : EntityNotFoundException("Fine not found")
class FineAlreadyPaidException : BusinessException("Fine already paid")
class UserHaveFineException : BusinessException("User has an outstanding fine")
