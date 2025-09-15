package com.gft.inditex_pricer.adapter.inbound

import com.gft.inditex_pricer.adapter.inbound.dto.PriceNotFoundException
import com.gft.inditex_pricer.adapter.inbound.dto.toResponseDTO
import com.gft.inditex_pricer.application.GetApplicablePriceUseCase
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime

@RestController
@RequestMapping("/api/v1/prices")
class PriceController(
    private val useCase: GetApplicablePriceUseCase
) {

    @GetMapping
    fun getApplicablePrice(
        @NotBlank @RequestParam productId: String,
        @NotBlank @RequestParam brandId: String,
        @RequestParam applicationDate: String
    ): ResponseEntity<Any> {
        val at = ZonedDateTime.parse(applicationDate)
        val price = useCase.retrieveBy(productId, brandId, at)
            ?: throw PriceNotFoundException()

        return ResponseEntity.ok(price.toResponseDTO())
    }
}
