package com.gft.inditex_pricer.adapter.outbound.persistence


import com.gft.inditex_pricer.domain.model.Money
import com.gft.inditex_pricer.domain.model.Price
import com.gft.inditex_pricer.domain.port.PriceQueryPort
import org.springframework.stereotype.Component
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Component
class PriceRepositoryAdapter(
    private val repository: SpringDataPriceRepository
) : PriceQueryPort {

    override fun findApplicable(productId: String, brandId: String, at: ZonedDateTime): List<Price> {
        val all = repository.findAll()

        return all
            .filter { it.productId == productId && it.brandId == brandId }
            .filter { at.toLocalDateTime().isAfter(it.startDate) || at.toLocalDateTime().isEqual(it.startDate) }
            .filter { at.toLocalDateTime().isBefore(it.endDate) }
            .map {
                Price( //TODO: use mapper function
                    productId = it.productId,
                    brandId = it.brandId,
                    priceList = it.priceList,
                    startDate = it.startDate.atZone(ZoneId.systemDefault()),
                    endDate = it.endDate.atZone(ZoneId.systemDefault()),
                    priority = it.priority,
                    price = Money(it.price, Currency.getInstance(it.currency))
                )
            }
    }
}