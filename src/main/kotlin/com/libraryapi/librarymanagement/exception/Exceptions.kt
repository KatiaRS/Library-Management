package com.libraryapi.librarymanagement.exception

class BookNotFoundException : Exception("Book not found")
class BookAlreadyExistsException : Exception("This book already exists")
class UserAlreadyExistsException : Exception("This user already exists")
class UserNotFoundException : Exception("User not found")
class ConversionIdException : Exception("Invalid Id")
class DocumentCannotBeChangedException : Exception("Document cannot be changed")
class UserReachedLoanLimitException : Exception("Loan limit reached by user")
class NoCopiesAvailableException : Exception("No books available")
