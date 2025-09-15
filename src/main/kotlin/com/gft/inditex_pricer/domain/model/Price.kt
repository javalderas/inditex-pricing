package com.gft.inditex_pricer.domain.model

import java.time.ZonedDateTime
import java.util.Currency

data class Money(val amount: Double, val currency: Currency)

data class Price(
    val productId: String,
    val brandId: String,
    val priceList: String,
    val startDate: ZonedDateTime,
    val endDate: ZonedDateTime,
    val priority: Int,
    val price: Money
)
