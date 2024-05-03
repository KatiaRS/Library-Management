package com.libraryapi.librarymanagement.rest

import com.libraryapi.librarymanagement.exception.BusinessException
import com.libraryapi.librarymanagement.exception.EntityNotFoundException
import com.libraryapi.librarymanagement.rest.dto.LoanDto
import com.libraryapi.librarymanagement.rest.dto.convertLoanId
import com.libraryapi.librarymanagement.rest.dto.toDto
import com.libraryapi.librarymanagement.rest.dto.toEntity
import com.libraryapi.librarymanagement.service.LoanService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/loans")
class LoanController(
    private val loanService: LoanService
) {

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody loanDto: LoanDto): LoanDto {
        try {
            return loanService.create(loanDto.toEntity()).toDto()
        } catch (e: EntityNotFoundException) {
            throw BusinessException(e.message)
        } catch (e: Exception) {
            throw e
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): LoanDto {
        return loanService.getById(convertLoanId(id)).toDto()
    }

    @PutMapping("/{id}/devolution")
    fun devLoan(@PathVariable id: String): LoanDto {
        return loanService.devLoan(convertLoanId(id))!!.toDto()
    }
}
