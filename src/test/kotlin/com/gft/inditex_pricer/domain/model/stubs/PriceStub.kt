package com.gft.inditex_pricer.domain.model.stubs

import com.gft.inditex_pricer.domain.model.Money
import com.gft.inditex_pricer.domain.model.Price
import java.time.ZonedDateTime
import java.util.*

object PriceStub {

    fun invoke(
        productId: String = "35455",
        brandId: String = "1",
        priceList: String = "1",
        startDate: String = "2020-06-14T00:00:00Z",
        endDate: String = "2020-12-31T23:59:59Z",
        priority: Int = 0,
        amount: Double = 35.50,
        currency: String = "EUR"
    ): Price {
        return Price(
            productId = productId,
            brandId = brandId,
            priceList = priceList,
            startDate = ZonedDateTime.parse(startDate),
            endDate = ZonedDateTime.parse(endDate),
            priority = priority,
            price = Money(amount, Currency.getInstance(currency))
        )
    }
}