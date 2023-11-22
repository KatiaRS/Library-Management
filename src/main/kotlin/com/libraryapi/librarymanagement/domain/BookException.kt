package com.libraryapi.librarymanagement.domain

class BookNotFoundException: Exception("Book not found")

class BookAlreadyExistsException: Exception("This book already exists")