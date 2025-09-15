package com.gft.inditex_pricer.application

import com.gft.inditex_pricer.domain.model.Price
import com.gft.inditex_pricer.domain.port.PriceQueryPort
import com.gft.inditex_pricer.domain.service.PriceResolver
import java.time.ZonedDateTime

class GetApplicablePriceUseCase(
    private val priceQueryPort: PriceQueryPort
) {
    fun execute(productId: String, brandId: String, applicationDate: ZonedDateTime): Price? {
        val candidates = priceQueryPort.findApplicable(productId, brandId, applicationDate)
        return PriceResolver.resolve(candidates, applicationDate)
    }
}