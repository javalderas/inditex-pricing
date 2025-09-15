package com.gft.inditex_pricer.adapter.outbound.persistence.stubs

import com.gft.inditex_pricer.adapter.outbound.persistence.entity.JpaPriceEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId

object JpaPriceEntityStub {

    fun invoke(
        productId: String = "35455",
        brandId: String = "1",
        priceList: String = "1",
        startDate: String = "2020-06-14T00:00:00",
        endDate: String = "2020-12-31T23:59:59",
        priority: Int = 0,
        price: Double = 35.50,
        currency: String = "EUR"
    ): JpaPriceEntity {
        return JpaPriceEntity(
            id = 1L,
            productId = productId,
            brandId = brandId,
            priceList = priceList,
            startDate = LocalDateTime.parse(startDate).atZone(ZoneId.systemDefault()),
            endDate = LocalDateTime.parse(endDate).atZone(ZoneId.systemDefault()),
            priority = priority,
            price = BigDecimal.valueOf(price).toDouble(),
            currency = currency
        )
    }
}