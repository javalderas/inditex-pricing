package com.gft.inditex_pricer.adapter.inbound
import com.gft.inditex_pricer.adapter.inbound.dto.ApiErrorDTO
import com.gft.inditex_pricer.adapter.inbound.dto.MoneyDTO
import com.gft.inditex_pricer.adapter.inbound.dto.PriceResponseDTO
import com.gft.inditex_pricer.application.GetApplicablePriceUseCase
import com.gft.inditex_pricer.domain.model.Price
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

@RestController
@RequestMapping("/api/v1")
class PriceController(
    private val getApplicablePriceUseCase: GetApplicablePriceUseCase
) {

    @GetMapping("/prices")
    fun getApplicablePrice(
        @RequestParam(required = true) productId: String?,
        @RequestParam(required = true) brandId: String?,
        @RequestParam(required = true) applicationDate: String?
    ): ResponseEntity<Any> {
        if (productId.isNullOrBlank() || brandId.isNullOrBlank() || applicationDate.isNullOrBlank()) {
            return ResponseEntity.badRequest()
                .body(ApiErrorDTO("MISSING_PARAMETER", "productId, brandId and applicationDate are required"))
        }

        val at = try {
            ZonedDateTime.parse(applicationDate)
        } catch (e: DateTimeParseException) {
            return ResponseEntity.badRequest()
                .body(ApiErrorDTO("INVALID_DATE", "applicationDate must follow ISO-8601 format"))
        }

        val result: Price? = getApplicablePriceUseCase.execute(productId, brandId, at)

        return when (result) {
            null -> ResponseEntity.status(404)
                .body(ApiErrorDTO("PRICE_NOT_FOUND", "No applicable price found for given parameters"))
            else -> ResponseEntity.ok(
                PriceResponseDTO(
                    productId = result.productId,
                    brandId = result.brandId,
                    priceList = result.priceList,
                    startDate = result.startDate.toString(),
                    endDate = result.endDate.toString(),
                    priority = result.priority,
                    price = MoneyDTO(result.price.amount, result.price.currency.currencyCode)
                )
            )
        }
    }
}