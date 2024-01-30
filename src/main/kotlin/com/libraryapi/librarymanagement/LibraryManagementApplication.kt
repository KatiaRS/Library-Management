package com.libraryapi.librarymanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
class LibraryManagementApplication

fun main(args: Array<String>) {
    runApplication<LibraryManagementApplication>(*args)
}
