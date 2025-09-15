package com.gft.inditex_pricer.adapter.outbound.persistence


import com.gft.inditex_pricer.adapter.outbound.persistence.entity.toDomain
import com.gft.inditex_pricer.domain.model.Price
import com.gft.inditex_pricer.domain.port.PriceQueryPort
import org.springframework.stereotype.Component
import java.time.ZonedDateTime


@Component
class PriceRepositoryAdapter(
    private val repository: SpringDataPriceRepository
) : PriceQueryPort {

    override fun findApplicable(productId: String, brandId: String, at: ZonedDateTime): List<Price> {
        return repository.findAll()
            .asSequence()
            .filter { it.productId == productId && it.brandId == brandId }
            .map { it.toDomain() }
            .toList()
    }
}