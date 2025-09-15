package com.gft.inditex_pricer.adapter.inbound.dto

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
data class ApiErrorDTO(val code: String, val message: String)