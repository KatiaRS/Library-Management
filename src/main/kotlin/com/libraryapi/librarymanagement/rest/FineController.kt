package com.libraryapi.librarymanagement.rest

import com.libraryapi.librarymanagement.rest.dto.FineDto
import com.libraryapi.librarymanagement.rest.dto.convertFineId
import com.libraryapi.librarymanagement.rest.dto.toDto
import com.libraryapi.librarymanagement.service.FineService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/fines")
class FineController(
    private val fineService: FineService
) {
    @PutMapping("/{fineId}/pay")
    fun payFine(@PathVariable fineId: String): FineDto {
        return fineService.payFine(convertFineId(fineId)).toDto()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): FineDto {
        return fineService.getById(convertFineId(id)).toDto()
    }
}
