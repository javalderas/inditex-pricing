package com.gft.inditex_pricer.adapter.outbound.persistence.entity

import com.gft.inditex_pricer.domain.model.Money
import com.gft.inditex_pricer.domain.model.Price
import jakarta.persistence.*
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "PRICES")
data class JpaPriceEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "BRAND_ID", nullable = false)
    val brandId: String,

    @Column(name = "START_DATE", nullable = false)
    val startDate: ZonedDateTime,

    @Column(name = "END_DATE", nullable = false)
    val endDate: ZonedDateTime,

    @Column(name = "PRICE_LIST", nullable = false)
    val priceList: String,

    @Column(name = "PRODUCT_ID", nullable = false)
    val productId: String,

    @Column(name = "PRIORITY", nullable = false)
    val priority: Int,

    @Column(name = "PRICE", nullable = false)
    val price: Double,

    @Column(name = "CURR", nullable = false)
    val currency: String
)

fun JpaPriceEntity.toDomain(): Price =
    Price(
        productId = this.productId,
        brandId = this.brandId,
        priceList = this.priceList,
        startDate = this.startDate.withZoneSameInstant(ZoneOffset.UTC),
        endDate = this.endDate.withZoneSameInstant(ZoneOffset.UTC),
        priority = this.priority,
        price = Money(this.price, Currency.getInstance(this.currency))
    )
