package com.gft.inditex_pricer.domain.port

import com.gft.inditex_pricer.domain.model.Price
import java.time.ZonedDateTime

interface PriceQueryPort {
    fun findApplicable(productId: String, brandId: String, applicationDate: ZonedDateTime): List<Price>
}
