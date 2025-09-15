package com.gft.inditex_pricer.adapter.inbound.dto

import com.gft.inditex_pricer.domain.model.Price
import java.time.format.DateTimeFormatter

data class MoneyDTO(val amount: Double, val currency: String)
data class PriceResponseDTO(
    val productId: String,
    val brandId: String,
    val priceList: String,
    val startDate: String,
    val endDate: String,
    val priority: Int,
    val price: MoneyDTO
)

private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

fun Price.toResponseDTO(): PriceResponseDTO =
    PriceResponseDTO(
        productId = this.productId,
        brandId = this.brandId,
        priceList = this.priceList,
        startDate = this.startDate.format(formatter),
        endDate = this.endDate.format(formatter),
        priority = this.priority,
        price = MoneyDTO(
            amount = this.price.amount,
            currency = this.price.currency.currencyCode
        )
    )